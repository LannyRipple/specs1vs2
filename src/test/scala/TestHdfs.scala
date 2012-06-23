package me.ljr.eg

import java.io._

import org.apache.hadoop.fs

import org.specs2.mutable._
import org.specs2.specification._

class TestHdfs extends SpecificationWithJUnit {

  override
  def map(fs: => Fragments): Fragments = {
    val dbs = Step {
      HdfsTestCluster.start()
    }
    val das = Step {
      HdfsTestCluster.shutdown()
    }

    dbs ^ fs ^ das
  }

  "HDFS" should {

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

      val str = new String(buf.slice(0, len - 1), "UTF-8")

      str mustEqual "xyzzy"
    }
  }
}
