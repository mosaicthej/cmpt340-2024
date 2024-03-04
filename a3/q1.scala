package Q1

import scala.collection.immutable

class Q1 {
    val digMap = Map(0 -> "zero", 1 -> "one", 2 -> "two", 3 -> "three", 4 -> "four", 5 -> "five", 6 -> "six", 7 -> "seven", 8 -> "eight", 9 -> "nine")
    
    val royalParent = Map(
        "George"    -> ("m", "William", "Catherine"), 
        "Charlotte" -> ("f", "William", "Catherine"), 
        "Louis"     -> ("m", "William", "Catherine"), 
        "Archie"    -> ("m", "Harry", "Meghan"),  
        "Lilibet"   -> ("f", "Harry", "Meghan"), 
        "Savannah"  -> ("f", "Autumn", "Peter"), 
        "Isla"      -> ("f", "Autumn", "Peter"), 
        "Mia"       -> ("f", "Zara", "Mike"), 
        "Lena"      -> ("f", "Zara", "Mike"), 
        "Lucas"     -> ("m", "Zara", "Mike"), 
        "Sienna"    -> ("f", "Beatrice", "Edoardo"), 
        "August"    -> ("m", "Eugenie", "Jack"), 
        "Beatrice"  -> ("f", "Andrew", "Sarah"), 
        "Eugenie"   -> ("f", "Andrew", "Sarah"), 
        "Louise"    -> ("f", "Edward", "Sophie"), 
        "James"     -> ("m", "Edward", "Sophie"), 
        "Peter"     -> ("m", "Mark", "Anne"), 
        "Zara"      -> ("f", "Mark", "Anne"), 
        "William"   -> ("m", "Diana", "Charles"), 
        "Harry"     -> ("m", "Diana", "Charles"), 
        "Charles"   -> ("m", "Elizabeth", "Philip"),
        "Anne"      -> ("f", "Elizabeth", "Philip"), 
        "Andrew"    -> ("m", "Elizabeth", "Philip"), 
        "Edward"    -> ("m", "Elizabeth", "Philip"), 
        "Elizabeth" -> ("f", "", ""), 
        "Philip"    -> ("m", "", ""), 
        "Diana"     -> ("f", "", ""), 
        "Mark"      -> ("m", "", ""), 
        "Sophie"    -> ("f", "", ""), 
        "Sarah"     -> ("f", "", ""), 
        "Mike"      -> ("m", "", ""), 
        "Autumn"    -> ("f", "", ""), 
        "Meghan"    -> ("f", "", ""), 
        "Catherine" -> ("f", "", ""), 
        "Timothy"   -> ("m", "", ""), 
        "Jack"      -> ("m", "", ""), 
        "Camilla"   -> ("f", "", ""), 
        "Edoardo"   -> ("m", "", ""))

    /* Notice that because of the primary focus 
       on direct descendants of Queen Elizabeth, 
       the amount of information available depends on 
       whether one is a descendant of Queen Elizabeth or not.  
       
       Particularly, note that there is no parentage information 
       available for the Queen, Prince Philip, 
       and for spouses of their descendants.  
    */
    /*
       Now, implement the following functions to 
       extract information about particular 
       family relationships from this Map.
   */
    val errFmtStr = "Error: %s has no %s on the map"

   /* a) [5 Points] 
   def parents(p: String): Either[String, (String, String)] 
    that takes a name p and returns either:
        - an error message if p is not in the Map,
        - an error message if p is in the Map but has no parentage information,
        - or a pair of strings representing the parents of p.
   */
    def parents(p: String): Either[String, (String, String)] = {    
        royalParent.get(p) match {
            case Some((_, "", "")) => Left(errFmtStr.format(p, "parent"))
            case Some((_, f, m)) => Right((f, m))
            case None => Left(errFmtStr.format(p, "entry"))
        }
    }

    /* b) [5 Points] this assumes existence of correct implementation of (a)
    def grandparents(p: String): Either[String, List[String]]
    that takes a name p and returns either:
        - an error message if p is not in the Map,
        - an error message if p is in the Map but has no grandparentage information,
        - or a list of strings representing the grandparents of p.

    First get the parents of p, then get the parents of each parent, 
    using a for-comprehension to combine the results.
    */
    def grandparents(p: String): Either[String, List[String]] = {
        parents(p) match {
            case Left(e) => Left(e) // error of any kind
            case Right((f, m)) => { // has a father and mother on the map
                val fGrand = parents(f) match {
                    case Left(e) => Nil
                    case Right((ff, fm)) => List(ff, fm)
                }
                val mGrand = parents(m) match {
                    case Left(e) => Nil
                    case Right((mf, mm)) => List(mf, mm)
                }
                
                val grandList = fGrand ++ mGrand
                grandList match {
                    case Nil => Left(errFmtStr.format(p, "grandparent"))
                    case _ => Right(grandList)  
                }
            }
        }
    }

}