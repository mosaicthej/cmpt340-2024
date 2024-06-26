package a3q1

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

    /* c) [5 Points]
    def aunts(p: String): Either[String, List[String]]. 
    female siblings of one's parents, 
    and female spouses of (male or female) siblings of one's parents
    
    that takes a name p and returns either:
        - an error message if p is not in the Map,
        - an error message if p is in the Map but has no aunt information,
        - or a list of strings representing the aunts of p.
    */

    /*
        it would be more helpful to have functions that:
    def siblings(p: String): Either[String, List[String]] -> returns siblings of p in a list:
        first find the parents, then find all children of the parents
        def children(p: String): Either[String, List[String]] -> returns children of p in a list:
            traverse the map and find all parents that are p
    def spouse(p: String): Either[String, String] -> returns spouse of p
        traverse the map and find the spouse of p
    */
    def children(p: String): Either[String, List[String]] = {
        val childrenList = royalParent.filter { case (k, v) => v._2 == p || v._3 == p }.keys.toList
        childrenList match {
            case Nil => Left(errFmtStr.format(p, "children"))
            case _ => Right(childrenList)
        }
    }

    def siblings(p: String): Either[String, List[String]] = {
        parents(p) match {
            case Left(e) => Left(e) // error of any kind
            case Right((f, m)) => { // has a father and mother on the map
                val sibList = children(f) match { // children of k's father
                    case Left(e) => Nil
                    case Right(fList) => fList.filter(_ != p) // remove k
                }
                /* no divorce or remarriage in the royal family,
                so no need to check for children of m */
                sibList match {
                    case Nil => Left(errFmtStr.format(p, "sibling"))
                    case _ => Right(sibList)
                }
            }
        }
    }

    def spouse(p: String): Either[String, String] = {
        val couples = royalParent.filter { case (k, v) => v._2 == p || v._3 == p }
        if (couples.isEmpty) Left(errFmtStr.format(p, "spouse"))
        else couples.values.head match {
            case (_, mo, fa) => if (mo == p) Right(fa) else Right(mo)
        }
    }

    /* get male and female siblings of p */
    def genderSibling(p: String): Either[String, (List[String], List[String])] = {
        siblings(p) match {
            case Left(e) => Left(e)
            case Right(sibList) => {
                val maleSib = sibList.filter(royalParent(_)._1 == "m")
                val femaleSib = sibList.filter(royalParent(_)._1 == "f")
                Right(maleSib, femaleSib)
            }
        }
    }

    /* now, implement the aunts.
     aunts are:
        - female siblings of one's parents
        - spouse of male siblings of one's parents
    */
    def aunts(p: String): Either[String, List[String]] = {
        parents(p) match {
            case Left(e) => Left(e) // error of any kind
            case Right((f, m)) => { // has a father and mother on the map
                val auntsL = List(f, m).flatMap(
                    x => genderSibling(x) match{
                        case Left(value) => Nil: List[String]
                        case Right((mSib, fSib)) => fSib ++ 
                            mSib.map(spouse(_))
                                .filter(_.isRight)
                                .map(_.right.get)
                    }
                )
                Right(auntsL)
            }
        }
    }

    /* d) [5 Points]  assume existence of correct implementations of (a) and (b)
    def firstCousinsSpouses(p: String): Either[String, List[String]]. 
    spouses of one's first cousins
    p -> (fa, mo) -> uncleaunt:=[fa's siblings]++[mo's siblings]
        -> firstcousions := uncleaunt.flatMap(children) -> result: firstcousions.map(spouse)
    */
    def firstCousinsSpouses(p: String): Either[String, List[String]] = {
        parents(p) match {
            case Left(e) => Left(e) // error of any kind
            case Right((f, m)) => { // has a father and mother on the map
                val uncleAunt = List(f, m).flatMap(
                    x => siblings(x) match{
                        case Left(e) => Nil: List[String]
                        case Right(sibList) => sibList
                    }
                )
                val firstCousins = uncleAunt.flatMap(
                    x => children(x) match{
                        case Left(value) => Nil: List[String]
                        case Right(cList) => cList
                    }
                )
                val firstCousinSpouses = firstCousins.map(spouse(_)).filter(_.isRight).map(_.right.get)
                Right(firstCousinSpouses)
            }
        }
    }

    /* e) [5 Points] assume correct implementations of (a) and (b) and (c)
    def unclesInLaw(p: String): Either[String, List[String]] 
    husbands of one's parents' (male or female) siblings  
    
    p -> (fa, mo) -> directAunts := [fa's femSiblings]++[mo's femSiblings]
        -> unclesInLaw := directAunts.map(spouse)
    */
    def unclesInLaw(p: String): Either[String, List[String]] = {
        parents(p) match {
            case Left(e) => Left(e) // error of any kind
            case Right((f, m)) => { // has a father and mother on the map
                val directAunts = List(f, m).flatMap(
                    x => genderSibling(x) match{
                        case Left(value) => Nil: List[String]
                        case Right((_, fSib)) => fSib
                    }
                )
                val unclesInLaw = directAunts.map(spouse(_)).filter(_.isRight).map(_.right.get)
                Right(unclesInLaw)
            }
        }
    }
}