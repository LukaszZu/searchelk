/**
  * Created by zulk on 26.11.16.
  */
class Kaa {
  def testScala(s: String): String = {
    s
  }

  def tr():Trait = {
    new Kaa with Trait
  };
}


object  KaaObj {
  def main(args: Array[String]): Unit = {
    val k = new Kaa with Trait
    print(k.traitt())
  }
}
