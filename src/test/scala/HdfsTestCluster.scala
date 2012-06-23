package me.ljr.eg

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hdfs.MiniDFSCluster

object HdfsTestCluster {

  private var dfsCluster  : MiniDFSCluster = null

  protected
  def hadoopVoodooInit() {
    def maybeSetProperty(key: String, default: String) {
      System.setProperty(key, System.getProperty(key, default))
    }

    maybeSetProperty("hadoop.log.dir", System.getProperty("test.build.logs", "build/test/logs"))
  }

  /**
   * Starts a HDFS cluster (2 HDFS instances)
   *
   * If the cluster is already running this method does nothing.
   *
   * @param reformatDFS indicates if DFS has to be reformated
   * @param props configuration properties to inject to the mini cluster (default: Map[String,String]())
   *
   * @throws Exception if the cluster could not be started
   */
  def start(reformatDFS: Boolean = true,
                       props: Map[String, String] = Map(),
                       conf: Configuration = new Configuration()) {
    if (dfsCluster != null) return

    hadoopVoodooInit()

    props.foreach {case (k, v) => conf.set(k, v)}

    dfsCluster = new MiniDFSCluster(conf, 2, reformatDFS, null)
  }

  /**
   * Stops the HDFS cluster
   *
   * If the cluster is already stopped this method does nothing.
   *
   * @throws Exception if the cluster could not be stopped
   */
  def shutdown() {
    if (dfsCluster != null) {
      dfsCluster.shutdown()
      dfsCluster = null
    }
  }

  /**
   *  Returns a preconfigured FileSystem instance to read and write files to it.
   *
   * @return the filesystem used by Hadoop.
   *
   * @throws IOException
   */
  def getFileSystem: FileSystem = dfsCluster.getFileSystem
}
