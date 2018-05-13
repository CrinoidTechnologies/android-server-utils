# android-server-utils
Template and boilerplate code for list, detail and edit screens for App

# Installation

Gradle:

Step 1: Add it in your root build.gradle at the end of repositories:
```groovy 
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2: Add the dependency
```groovy 
	dependencies {
	        implementation 'com.github.CrinoidTechnologies:android-server-utils:v0.5.0'
	}
 ``` 
Maven:

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Step 2. Add the dependency

```xml
	<dependency>
	    <groupId>com.github.CrinoidTechnologies</groupId>
	    <artifactId>android-server-utils</artifactId>
	    <version>v0.5.0</version>
	</dependency>
```
