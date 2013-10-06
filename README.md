dynamodb-unittest
=================
 This repositroy contains two projects : <br />
 nosqlunit-dynamoDB : a junit extension<br />
 dynamoDB-unittest-demo : shows how to use "nosqlunit-dynamoDB"


 The objectif of this project is to create a junit extension that allows to inialize DynamdoDB data base for each   methods with the provided data sets.
 This project depends on another project (which must be installed in the maven repository) available on this link  https://github.com/mboudreau/Alternator/. This project will launch a DynamdoDB server locally and provided a client to access it.

 To get started, you need to follow the following steps:
 1. Install the project in local repository and add the following dependency in your project.
 ```xml
<dependency>
  <groupId>com.ando</groupId>
  <artifactId>nosqlunit-dynamdodb</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
```

 2. In your project classpath (for example src/test/resources/ directory, create a file named "init_db.json".
 This file will describe the structure of tables. It must have the following format :
```xml
{
  "Table":[{
		"AttributeDefinitions": [{
			"AttributeName": String,
			"AttributeType": N/S/B/SN/SS/SB
		}],
		"TableName": String,
		"KeySchema": [{
			"AttributeName": String,
			"KeyType": "HASH"/"RANGE"
		}],
		"ProvisionedThroughput": {
			"ReadCapacityUnits": Number,
			"WriteCapacityUnits": Number
		}
	}]
}
```
you find an exmaple in the demo project

 3. Define your data set in JSON file ans put it in your project. It must have the following format :
```xml
{
	"table_name": [{
		"attribute_name": "attribute_value"
	}]
}
```
you find an exmaple in the demo project

 4. In the test class add this annotation
```xml
@DataSetLocation(value = "/dataset_file_location")
```
If this annotation is not specified, the extension search the data set file with name for the test class.
for example, if your test class's name is x.y.z.TestClass, default file name path will be "/x/y/z/z-dataset.json"

 5. In the test class add this rule as attribute
```xml
@Rule
public DynamoDBRule rule = new DynamoDBRule();
```

