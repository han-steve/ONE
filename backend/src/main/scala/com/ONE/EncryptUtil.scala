package com.ONE

import java.math.BigInteger
import java.security.{MessageDigest, NoSuchAlgorithmException}


private[ONE] object EncryptUtil {

  private val sfSalt = "9e107d9d372bb6826bd81d3542a419d6"

  def getMd5(input: String): String = try { // Static getInstance method is called with hashing MD5
    val md = MessageDigest.getInstance("MD5")
    // digest() method is called to calculate message digest
    //  of an input digest() return array of byte
    val messageDigest = md.digest((input + sfSalt).getBytes)
    // Convert byte array into signum representation
    val no = new BigInteger(1, messageDigest)
    // Convert message digest into hex value
    var hashtext = no.toString(16)
    while ({
      hashtext.length < 32
    }) hashtext = "0" + hashtext
    hashtext
  } catch {
    case e: NoSuchAlgorithmException =>
      throw new RuntimeException(e)
  } // For specifying wrong message digest algorithms

}
