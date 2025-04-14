  package ex1

import ex1.*
import org.junit.Assert.*
import org.junit.Test
import org.scalatest.matchers.should.Matchers.*

class ParserTests extends org.scalatest.flatspec.AnyFlatSpec:

  def parser = new BasicParser(Set('a', 'b', 'c'))
  // Note NonEmpty being "stacked" on to a concrete class
  // Bottom-up decorations: NonEmptyParser -> NonEmpty -> BasicParser -> Parser
  def parserNE = new NonEmptyParser(Set('0', '1'))
  def parserNTC = new NotTwoConsecutiveParser(Set('X', 'Y', 'Z'))
  // note we do not need a class name here, we use the structural type
  def parserNTCNE = new BasicParser(Set('X', 'Y', 'Z')) with NotTwoConsecutive[Char] with NonEmpty[Char]
  def sparser: Parser[Char] = "abc".charParser()

  "a basic parser " should "accept empty strings or only specified chars" in:
    parser.parseAll("aabc".toList) should be (true)
    parser.parseAll("aabcdc".toList) should be (false)
    parser.parseAll("".toList) should be (true)

  "a not empty parser" should "accept only specified chars" in:
    parserNE.parseAll("0101".toList) should be (true)
    parserNE.parseAll("0123".toList) should be (false)
    parserNE.parseAll(List()) should be (false)

  "a not two consecutive parser" should "accept empty strings or not consecutive specified chars" in:
    parserNTC.parseAll("XYZ".toList) should be (true)
    parserNTC.parseAll("XYYZ".toList) should be (false)
    parserNTC.parseAll("".toList) should be (true)

  "a not two consecutive and not empty parser" should "not accept consecutive specified chars" in :
    parserNTCNE.parseAll("XYZ".toList) should be(true)
    parserNTCNE.parseAll("XYYZ".toList) should be(false)
    parserNTCNE.parseAll("".toList) should be(false)

  "a string parser" should "only accept string chars" in:
    sparser.parseAll("aabc".toList) should be (true)
    sparser.parseAll("aabcdc".toList) should be (false)
    sparser.parseAll("".toList) should be (true)

  "a shorter than N parser" should "accept only string sequences shorter or equal to n" in:
    def parserSTN = new ShorterThanNParser(Set('a', 'b', 'c'), 5)
    parserSTN.parseAll("abb".toList) should be (true)
    parserSTN.parseAll("abbd".toList) should be (false)
    parserSTN.parseAll("abbccc".toList) should be (false)

//  @Test
//  def testBasicParser =
//    assertTrue(parser.parseAll("aabc".toList))
//    assertFalse(parser.parseAll("aabcdc".toList))
//    assertTrue(parser.parseAll("".toList))

//  @Test
//  def testNotEmptyParser =
//    assertTrue(parserNE.parseAll("0101".toList))
//    assertFalse(parserNE.parseAll("0123".toList))
//    assertFalse(parserNE.parseAll(List()))

//  @Test
//  def testNotTwoConsecutiveParser =
//    assertTrue(parserNTC.parseAll("XYZ".toList))
//    assertFalse(parserNTC.parseAll("XYYZ".toList))
//    assertTrue(parserNTC.parseAll("".toList))

//  @Test
//  def testNotEmptyAndNotTwoConsecutiveParser =
//    assertTrue(parserNTCNE.parseAll("XYZ".toList))
//    assertFalse(parserNTCNE.parseAll("XYYZ".toList))
//    assertFalse(parserNTCNE.parseAll("".toList))

//  @Test
//  def testStringParser =
//    assertTrue(sparser.parseAll("aabc".toList))
//    assertFalse(sparser.parseAll("aabcdc".toList))
//    assertTrue(sparser.parseAll("".toList))
