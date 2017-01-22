// spark-shell --jars ojdbc7.jar

// load libraries
import java.sql.Types
import org.apache.spark.sql.types._
import org.apache.spark.sql.jdbc.{JdbcDialects, JdbcType, JdbcDialect}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf,SparkContext}
import org.apache.hadoop.fs._;

// define stuff
def read_password(filepath:String): String ={sc.textFile(filepath).first}
val pwd = read_password("/home/pass.pwd")
val url = "jdbc:oracle:thin:@localhost:1521/orcl"

// read oracle table
val jobsDF = sqlContext.read.format("jdbc").options( Map("url"->url, "dbtable"-> "HR.JOBS", "user"->"HR", "password"-> pwd, "driver"-> "oracle.jdbc.OracleDriver")).load()
jobsDF.registerTempTable("jobs")
val jobs_1 = sqlContext.sql("select * from jobs")

// select data from oracle table
jobs_1.take(10).foreach(println)
jobsDF.show
