package ppa

import dsa.GIB.{getOPin2IPinConnect, getOPin2TrackConnect, getTrack2IPinConnect, getTrack2TrackConnect}

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Map}
import chisel3.util.log2Ceil
object ppa_gib {


  def getMux(numTrack: Int, numIOPinList: List[Int], pin2pin: List[Int], pin2track:List[Int], track2pin:List[Int], trackDirections : ListBuffer[Int]): List[Int] = {
    val totalconnect = (getTrack2TrackConnect(numTrack,trackDirections) ++ getOPin2TrackConnect(numTrack, List(numIOPinList(1), numIOPinList(3), numIOPinList(5), numIOPinList(7)), pin2track, trackDirections)++
      getTrack2IPinConnect(numTrack, List(numIOPinList(0), numIOPinList(2), numIOPinList(4), numIOPinList(6)), track2pin, trackDirections)++
      getOPin2IPinConnect(numIOPinList, pin2pin))
    val totalsink = ListBuffer[Seq[Int]]()
    //println("totalconnect: " + totalconnect)
    totalconnect.map{ins => totalsink.append(ins.tail.tail)}
    //println("totalsink: " + totalsink)
    val totalsinkdst = totalsink.distinct
    //println("totalsinkdst: " + totalsinkdst)
    val MuxList = totalsinkdst.map{dst =>totalsink.count( _== dst) }.toList
    //println("MuxList: " + MuxList)
    MuxList
  }

  def getgibarea(numTrack: Int, num_iopin_list : Map[String,Int], pin2pin: List[Int], pin2track:List[Int], track2pin:List[Int], trackDirections : ListBuffer[Int], track_reged :Boolean, maxII : Int): Double = {

    val numIOPinMap = num_iopin_list
    val nNWi = numIOPinMap("ipin_nw")  // number of the PE input pins on the NORTHWEST side of the GIB
    val nNWo = numIOPinMap("opin_nw")  // number of the PE output pins on the NORTHWEST side of the GIB
    val nNEi = numIOPinMap("ipin_ne")  // number of the PE input pins on the NORTHEAST side of the GIB
    val nNEo = numIOPinMap("opin_ne")  // number of the PE output pins on the NORTHEAST side of the GIB
    val nSEi = numIOPinMap("ipin_se")  // number of the PE input pins on the SOUTHEAST side of the GIB
    val nSEo = numIOPinMap("opin_se")  // number of the PE output pins on the SOUTHEAST side of the GIB
    val nSWi = numIOPinMap("ipin_sw")  // number of the PE input pins on the SOUTHWEST side of the GIB
    val nSWo = numIOPinMap("opin_sw")  // number of the PE output pins on the SOUTHWEST side of the GIB
    val numIOPinList = List(nNWi, nNWo, nNEi, nNEo, nSEi, nSEo, nSWi, nSWo)

    /*val fcMap = connect_flexibility
    val fci  = fcMap("num_itrack_per_ipin")     // ipin-itrack connection flexibility, connected track number, 2n
    val fco  = fcMap("num_otrack_per_opin")     // opin-otrack connection flexibility, connected track number, 2n
    val fcio = fcMap("num_ipin_per_opin")       // opin-ipin  connection flexibility, connected ipin number, 2n
    val fcList = List(fci, fco, fcio)*/


    val muxlist = getMux(numTrack, numIOPinList, pin2pin, pin2track, track2pin, trackDirections).sortBy(ins => ins)
    var area : Double = 0
    var index =0
    var rate : Double= 1
    //val muxarea = sm_area.ara_par("mux_area").asInstanceOf[mutable.Map[Int, Int]]
    muxlist.map{mux => {
      /*if(index == area_par.cycle){
        index = 0
        rate = rate*(1 + area_par.reduce_rate)
      }else {
        index += 1
      }
      area = area + area_par.area_mux32_map(mux)*rate*/
      //area = area + muxarea(mux)
      area = area + sm_area.area_mux32_map(mux)
    }}
    if(track_reged) {
      area = area + trackDirections.count( ins => ins == 1)*numTrack * sm_area.area_regnxt32
    }
    val summaxCfgWidth = muxlist.foldLeft(0){(a,b)=> a + log2Ceil(b)}
    //val cfgsBit = trackDirections.count( ins => ins == 1)*numTrack
    //println("trackDirections.count( ins => ins == 1)*numTrack: " + trackDirections.count( ins => ins == 1)*numTrack)
    //println("summaxCfgWidth: " + summaxCfgWidth)
    area = area + summaxCfgWidth*sm_area.area_cfg32_map(maxII)* sm_area.cfg_rate
    println("gib area :" + area)
    area
  }
}

