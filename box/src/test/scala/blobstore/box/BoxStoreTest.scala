package blobstore.box


import blobstore.Path
import cats.effect.{ContextShift, IO}
import com.box.sdk.BoxAPIConnection
import org.scalatest.FlatSpec
import org.scalatest.MustMatchers

import scala.concurrent.ExecutionContext

class BoxStoreTest extends FlatSpec with MustMatchers {

  implicit val ec = ExecutionContext.global
  implicit val cs: ContextShift[IO] = IO.contextShift(ec)
  "splitPath" should "correctly split a long path" in {
    val boxStore = new BoxStore[IO](new BoxAPIConnection(""), "", ec)
    val testPath = Path("long/path/to/filename")
    val (pathToParentFolder, key) = boxStore.splitPath(testPath)
    pathToParentFolder must be("long" :: "path" :: "to" :: Nil)
    key must be("filename")
  }

  it should "split a single element path into a single element list and empty string key" in {
    val boxStore = new BoxStore[IO](new BoxAPIConnection(""), "", ec)
    val testPath = Path("filename")
    val (pathToParentFolder, key) = boxStore.splitPath(testPath)
    pathToParentFolder must be("filename"::Nil)
    key must be("")
  }

  it should "split an empty path into empty list, empty string key" in {
    val boxStore = new BoxStore[IO](new BoxAPIConnection(""), "", ec)
    val testPath = Path("")
    val (pathToParentFolder, key) = boxStore.splitPath(testPath)
    pathToParentFolder must be(""::Nil)
    key must be("")
  }

}