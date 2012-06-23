package me.ljr.eg

import java.io._

import org.apache.hadoop.fs

import org.specs.SpecificationWithJUnit

class TestHdfs extends SpecificationWithJUnit {

  doBeforeSpec {
    HdfsTestCluster.start()
  }

  doAfterSpec {
    HdfsTestCluster.shutdown()
  }

  def step[A](body: => A): A = body.isExpectation

  "HDFS" should {
    setSequential()

    val dfs = HdfsTestCluster.getFileSystem
    val path = new fs.Path("input")

    step {
      val dfsFh = dfs.create(path, true)
      val fh = new PrintStream(dfsFh.getWrappedStream, true, "UTF-8")
      fh.println("xyzzy")
      fh.close()
    }

    "be accessable" in {
      val fh = dfs.open(path)
      val buf = new Array[Byte](80)

      val len = fh.read(buf)
      fh.close()

      len must be_>(0)

      val str = new String(buf.slice(0,len-1), "UTF-8")

      str mustEqual "xyzzy"
    }
  }
}
