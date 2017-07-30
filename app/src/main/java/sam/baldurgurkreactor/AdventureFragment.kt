package sam.baldurgurkreactor

import android.app.Fragment
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_adventure.*

class AdventureFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_adventure,container,false)
    }

    var activeMarkers: MutableList<Pair<Int,Int>> = mutableListOf()
    var activeAbilityType: String = ""
    var isHeroDead: Boolean = false
    var activeSlot = 5

    var cooldowns = mutableMapOf<Int,Int>(0 to 0, 1 to 0, 2 to 0,3 to 0,4 to 0)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).currentPlayer.location = Pair(1,1)

        val p = (activity as MainActivity).currentPlayer.equipped

        button_head.setText(p[0]?.name)
        button_shoulders.setText(p[1]?.name)
        button_legs.setText(p[2]?.name)
        button_offHand.setText(p[3]?.name)
        button_mainHand.setText(p[4]?.name)

        button_head.setOnClickListener {
            activeSlot =0
            SelectAbility(activeSlot)
        }
        button_shoulders.setOnClickListener {
            activeSlot=1
            SelectAbility(activeSlot)
        }
        button_legs.setOnClickListener {
            activeSlot=2
            SelectAbility(activeSlot)
        }
        button_offHand.setOnClickListener {
            activeSlot = 3
            SelectAbility(activeSlot)
        }
        button_mainHand.setOnClickListener {
            activeSlot = 4
            SelectAbility(activeSlot)
        }

        gridView_adventure.adapter = ImageAdapter(context)

        gridView_adventure.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, _view: View?, position: Int, id: Long) {
                if(CheckCooldown(activeSlot)){
                    if(activeMarkers.contains((gridView_adventure.adapter as ImageAdapter).CalculatePairFromPosition(position))){
                        (gridView_adventure.adapter as ImageAdapter).RemoveMarkers(activeMarkers)
                        (gridView_adventure.adapter as ImageAdapter).ActivateMonsters(true)
                        (gridView_adventure.adapter as ImageAdapter).notifyDataSetChanged()

                        cooldowns.put(activeSlot,p[activeSlot]?.cooldown?:0)
                        DecrementAllCooldowns()

                        Handler().postDelayed({HeroStage(position)},300)
                        Handler().postDelayed({SlowerStage()},600)
                    }
                }else Toast.makeText(context,"Cooldown ${cooldowns[activeSlot]}",Toast.LENGTH_SHORT).show()
            }
        })

        (gridView_adventure.adapter as ImageAdapter).PutHero(listOf(Pair(5,5)))

        button_backFromAdventure.setOnClickListener {
            val simpleAlert = AlertDialog.Builder(activity).create()
            simpleAlert.setTitle("Leave Adventure")
            simpleAlert.setMessage("Do you really want to leave this adventure?")
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
                }
            })
            simpleAlert.show()
        }
    }
    fun SelectAbility(slot: Int){
        val p = (activity as MainActivity).currentPlayer.equipped
        (activity as MainActivity).currentPlayer.current_speed=p.get(slot)?.ability?.speed?:0
        (gridView_adventure.adapter as ImageAdapter).RemoveMarkers(activeMarkers)
        activeAbilityType = p.get(slot)?.ability?.type?:""
        (gridView_adventure.adapter as ImageAdapter).PlaceMarkers(p.get(slot)?.ability?.relative_pairs?: listOf())
        (gridView_adventure.adapter as ImageAdapter).notifyDataSetChanged()
        activeMarkers = (gridView_adventure.adapter as ImageAdapter).CalculatePairsFromRelative(p.get(slot)?.ability?.relative_pairs?: listOf()) as MutableList<Pair<Int, Int>>
    }
    fun HeroStage(position:Int){
        CheckDeath()
        if(activeAbilityType == "move"){
            (gridView_adventure.adapter as ImageAdapter).PlaceHeroFromPosition(position)
        }else if (activeAbilityType == "attack"){
            (gridView_adventure.adapter as ImageAdapter).AttackFromPosition(position)
        }
        (gridView_adventure.adapter as ImageAdapter).notifyDataSetChanged()
        activeMarkers.clear()
        if(!isHeroDead){
            CheckDeath()
        }
    }
    fun SlowerStage(){
        if(!isHeroDead){
            (gridView_adventure.adapter as ImageAdapter).ClearLastMiss()
            (gridView_adventure.adapter as ImageAdapter).ActivateMonsters(false)
            (gridView_adventure.adapter as ImageAdapter).GenerateMonster()
            (gridView_adventure.adapter as ImageAdapter).notifyDataSetChanged()
            CheckDeath()

        }
    }
    fun CheckDeath(){
        if((gridView_adventure.adapter as ImageAdapter).CheckPlayerDeath()){
            isHeroDead = true
            val simpleAlert = AlertDialog.Builder(activity).create()
            simpleAlert.setTitle("You were struck down")
            simpleAlert.setMessage("You fell in battle, but recovered eventually..")
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {}
            })
            simpleAlert.show()
            fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
        }
    }
    fun CheckCooldown(type: Int):Boolean{
        if(cooldowns[type]==0){
            return true
        }else return false
    }
    fun DecrementAllCooldowns(){
        for(v in cooldowns){
            val old = v.value
            if(old>0){
                cooldowns.put(v.key,old-1)
            }
        }
    }
}