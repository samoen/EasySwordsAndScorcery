package sam.baldurgurkreactor

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import java.util.*

class ImageAdapter(private val mContext: Context) : BaseAdapter() {

    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(mContext)
            imageView.setLayoutParams(ViewGroup.LayoutParams(105,105))
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
            imageView.setPadding(3, 3, 3, 3)
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(mThumbIds[position])
        return imageView
    }
    fun PlaceMarkers(squares: List<Pair<Int,Int>>){
        val absos = CalculatePairsFromRelative(squares)
        for (v in absos){
            if(v.first in 1..10 && v.second in 1..10){
                mThumbIds.set(CalculatePositionFromPair(v),option)
            }
        }
    }
    fun RemoveMarkers(squares: List<Pair<Int,Int>>){
        for (v in squares){
            if(v.first in 1..10 && v.second in 1..10){
                var markerOnMonster = false
                for(m in monsters){
                    if(CalculatePositionFromPair(v) == m.value){
                        markerOnMonster = true
                    }
                }
                if(markerOnMonster){
                    mThumbIds.set(CalculatePositionFromPair(v),enemy)
                }else{
                    mThumbIds.set(CalculatePositionFromPair(v),emptySquare)

                }
            }
        }
    }
    fun PutHero(squares: List<Pair<Int,Int>>){
        val absos = CalculatePairsFromRelative(squares)
        for (v in absos){
            mThumbIds.set(CalculatePositionFromPair(v),player)
        }
        (mContext as MainActivity).currentPlayer.location = Pair(absos.get(0).first,absos.get(0).second)
    }
    fun PlaceHeroFromPosition(position: Int){
        mThumbIds.set(position,player)
        mThumbIds.set(CalculatePositionFromPair((mContext as MainActivity).currentPlayer.location?:Pair(1,1)),emptySquare)
        mContext.currentPlayer.location = CalculatePairFromPosition(position)
    }
    fun AttackFromPosition(position: Int){
        val newMonsters = monsters

        if(monsters.containsValue(position) ){
            newMonsters.remove(newMonsters.filter { it.value == position }.keys.toIntArray().get(0))
            mThumbIds.set(position,hitSquare)
            //Handler().postDelayed({ mThumbIds.set(position,emptySquare) },300)
            (mContext as MainActivity).currentPlayer.gold++
        }
        monsters = newMonsters
    }
    fun CalculatePairFromPosition(pos: Int): Pair<Int,Int>{
        val column = Math.ceil((pos.toDouble() + 1) / 10).toInt()
        val row = column * 10 - pos
        val mycol  = Math.abs(column-11)
        val myrow = Math.abs(row-11)
        return Pair(myrow,mycol)
    }

    fun GenerateMonster(){
        for(i in 0..40){
            if(!monsters.containsKey(i)){
                val r = Random().nextInt(9)+1
                mThumbIds.set(r,enemy)
                monsters.put(i,r)
                monsterSpeeds.put(i,Random().nextInt(10))
                break
            }
        }
    }

    fun ActivateMonsters(faster:Boolean){
        val newMonsters = monsters
        for(m in monsters){
            if( (monsterSpeeds.get(m.key)!!.toInt() >= (mContext as MainActivity).currentPlayer.current_speed && faster)|| (monsterSpeeds.get(m.key)!!.toInt() < (mContext as MainActivity).currentPlayer.current_speed && !faster)  ){

                val f = CalculatePairFromPosition(m.value).first + (Random().nextInt(2)-1)
                val s = CalculatePairFromPosition(m.value).second + (Random().nextInt(2)-1)
                val pos = CalculatePositionFromPair(Pair(f,s))
                if(f in 1..10 && s in 1..10 && !monsters.containsValue(pos)){
                    mThumbIds.set(m.value,emptySquare)
                    newMonsters.put(m.key,pos)
                    mThumbIds.set(pos,enemy)
                }
                //else{newMonsters.remove(newMonsters.filter { it.value == m.value }.keys.toIntArray().get(0)) }
            }
        }
        monsters = newMonsters
    }

    fun CheckPlayerDeath():Boolean{
        var dead = false
        for(m in monsters){
            if(m.value == CalculatePositionFromPair((mContext as MainActivity).currentPlayer.location?:Pair(1,1))){
                dead = true
            }
        }
        return dead
    }

    fun CalculatePositionFromPair(pair: Pair<Int,Int>):Int {
        when(pair.first){
            1->return 100-(pair.second*10)
            2->return 101-(pair.second*10)
            3->return 102-(pair.second*10)
            4->return 103-(pair.second*10)
            5->return 104-(pair.second*10)
            6->return 105-(pair.second*10)
            7->return 106-(pair.second*10)
            8->return 107-(pair.second*10)
            9->return 108-(pair.second*10)
            10->return 109-(pair.second*10)
        }
        return 0
    }
    fun CalculatePairsFromRelative(rel: List<Pair<Int,Int>>):List<Pair<Int,Int>> {
        val absolutes = mutableListOf<Pair<Int, Int>>()
        for (v in rel) {
            val p = Pair(((mContext as MainActivity).currentPlayer.location?.first as Int) + v.first, (mContext.currentPlayer.location?.second) as Int + v.second)
            absolutes.add(p)
        }
        return absolutes
    }

    var monsters = mutableMapOf<Int,Int>()
    var monsterSpeeds = mutableMapOf<Int,Int>()
    val option = R.drawable.abc_ic_arrow_drop_right_black_24dp
    val player = R.drawable.abc_ic_star_black_48dp
    val emptySquare = R.drawable.empty_square
    val hitSquare = R.drawable.abc_ic_voice_search_api_material
    val enemy = R.drawable.notification_bg
    private val mThumbIds = arrayOf<Int>(
            emptySquare, emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare
    )
}
