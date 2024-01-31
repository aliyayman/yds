package com.aliyayman.yds_app.viewmodel

import android.app.Application
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import kotlinx.coroutines.launch

class TestViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var sorularList: ArrayList<Word>
    private lateinit var yanlisListe: ArrayList<Word>
    private lateinit var dogruSoru: Word
    var soruSayac = 0
    var yanlisSayac = 0
    var dogruSayac = 0
    private val seceneklerKaristirmaList = HashSet<Word>()
    private val seceneklerList = ArrayList<Word>()


    fun init() : ArrayList<Word>  {
        launch {
            sorularList =
                myDatabase(getApplication()).wordDao().getWordTen(1).toList() as ArrayList<Word>
            println("sorularList:" + sorularList)
            dogruSoru = sorularList[soruSayac]
            println("dogru soru" + dogruSoru)
            yanlisListe =
                myDatabase(getApplication()).wordDao().getFaultWord3(1).toList() as ArrayList<Word>
            println("yanlıs liste:" + yanlisListe)

            seceneklerKaristirmaList.clear()
            seceneklerKaristirmaList.add(dogruSoru)
            seceneklerKaristirmaList.addAll(yanlisListe)
            seceneklerList.clear()
            seceneklerList.addAll(seceneklerKaristirmaList)
            println("seceneklerkarmaList:" + seceneklerList)

        }
        return seceneklerList

/*

        seceneklerKaristirmaList.clear()
        seceneklerKaristirmaList.add(Word("apple","elma",false,1))
       // seceneklerKaristirmaList.add(dogruSoru)
          seceneklerKaristirmaList.addAll(yanlisListe)
*/


        /*    seceneklerList.clear()
            seceneklerList.addAll(seceneklerKaristirmaList)*/


    }

    fun soruYuklee(): Pair<Word, List<Word>> {
        launch {
            dogruSoru = sorularList[soruSayac]
            println("dogru soru" + dogruSoru)
            yanlisListe =
                myDatabase(getApplication()).wordDao().getFaultWord3(1).toList() as ArrayList<Word>
            println("yanlıs liste:" + yanlisListe)
        }




        seceneklerKaristirmaList.clear()
        seceneklerKaristirmaList.add(dogruSoru)
        seceneklerKaristirmaList.addAll(yanlisListe)
        seceneklerList.clear()
        seceneklerList.addAll(seceneklerKaristirmaList)
        println("seceneklerkarmaList:" + seceneklerList)

        return Pair(dogruSoru, seceneklerList)
    }

    fun dogruKontrol(selectedAnswer: String): Boolean {
        val dogruCevap = dogruSoru.tc
        if (selectedAnswer == dogruCevap) {
            dogruSayac++
            return true
        }
        yanlisSayac++
        return false
    }

    fun nextQuestion() {
        soruSayac++
    }

    fun isQuizFinished(): Boolean {
        return soruSayac == 10
    }

    fun getScores(): Pair<Int, Int> {
        return Pair(dogruSayac, yanlisSayac)
    }
}