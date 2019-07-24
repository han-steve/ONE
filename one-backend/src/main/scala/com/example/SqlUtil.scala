package com.example

import java.sql.DriverManager

private[example] object SqlUtil {

  Class.forName("org.postgresql.ds.PGSimpleDataSource") // Load the driver

  lazy val SqlConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ONE", "timothytqin", "tq05112001") // Make the connection

}
