package dsa.common

import chisel3._
import chisel3.util._
import _root_.circt.stage.ChiselStage

class Register (width : Int) extends Module{
  val io = IO(new Bundle {
    val in = Input(UInt(width.W))
    val out = Output(UInt(width.W))
  })
  val out_ = RegNext(io.in)
  io.out := out_
}

object RegisterVerilogGen extends App {
  ChiselStage.emitSystemVerilogFile(new Register(32), args)
}
