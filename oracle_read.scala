// spark SQL example - connect to Oracle HR schema and pull data from table.
// to run: spark-shell --jars ojdbc7.jar

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
val jobsDF = sqlContext.read.format("jdbc").options(Map(
	"url"->url,
	"dbtable"-> "HR.JOBS",
	"user"->"HR",
	"password"-> pwd,
	"driver"-> "oracle.jdbc.OracleDriver"
	)).load()

jobsDF.registerTempTable("tmptbl_jobs")
val jobs_fltr = sqlContext.sql("select * from tmptbl_jobs where MIN_SALARY > '5000'")

// some selects to view data
jobs_fltr.take(5).foreach(println)
jobs_fltr.show

// create Hive table and write to HDFS
jobs_fltr.saveAsTable(default.jobsHDFS)