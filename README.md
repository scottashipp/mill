mill [![Build Status](https://travis-ci.com/scottashipp/mill.svg?branch=master)](https://travis-ci.com/scottashipp/mill)
====


![logo](media/mill-rind-90x90.png?raw=true)

Java library to make run-of-the-mill tasks more elegant. 

Compatible with Java 8+.

[![ko-fi](https://www.ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/A0A7147N8)

## In this README
* [How to add Mill to your application](#how-to-add-mill-to-your-application)
* [Some examples](#some-examples)
* [Contribution guidelines](#contribution-guidelines)
* [Where to find help](#where-to-find-help)
* [License](#license)

## How to add Mill to your application
If you are using Maven or another supported dependency management tool, you can use Jitpack to add Mill to your application.

### Maven
First, add Jitpack (if you haven't already) to the repositories section of your Maven pom.

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Second, add Mill to the dependencies section of your Maven pom.

```
<dependency>
    <groupId>com.github.scottashipp</groupId>
    <artifactId>mill</artifactId>
    <version>{{latest version tag}}</version>
</dependency>
```

Please see the [releases](https://github.com/scottashipp/mill/releases) to find the latest version tag, such as `v1.0`.

### Other

Please visit [Jitpack.io](https://jitpack.io/) for instructions for other popular tools like Gradle and sbt.


## Some examples

The following are just a few of the things you'll find in mill.

#### Fluent interface for null conditional operations
If you have a chain of calls into an object graph (such as `user.addresses().billingAddress().zipCode()`) and you care about avoiding a NullPointerException, you might write code like this:

```java
// standard java
if(user != null) {
    UserAddresses userAddresses = user.addresses();
    if(userAddresses != null) {
        Address billingAddress = userAddresses.billingAddress();
        if (billingAddress != null) {
            return billingAddress.zipCode();
        }
    }
}
```

With the `NullSafe` class, you can convert the above to:

```java
String zipCode = NullSafe.of(user)
                .call(User::addresses)
                .call(UserAddresses::billingAddress)
                .call(Address::zipCode)
                .get();
```

#### Join a stream of non-string objects together into a string
```java
Stream<Holiday> majorUsHolidays = Stream.of(newYears, easter, independenceDay, thanksgiving, christmas);

// standard Java requires us to first map Object::toString
majorUsHolidays.map(Object::toString).collect(Collectors.joining(", "));

// mill doesn't require the extra map call
majorUsHolidays.collect(MoreCollectors.joining(", "));
```

#### Filter a stream on multiple criteria

Given the following stream, suppose we want to find names with a maximum length of 12 and in the alphabetical range A-M.

```java
    Stream<String> engineeringTeam = Stream.of(
                null, "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                null, null, "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut", ""
        );
```

With standard Java we might apply the multiple filters in order like this.

```java
// standard java requires multiple filters
String staffAtoMWithShortNames = engineeringTeam
                .filter(Objects::nonNull)
                .filter(((Predicate<String>)String::isEmpty).negate())
                .filter(s -> s.compareTo("A") > 0 && s.compareTo("M") < 0)
                .filter(s -> s.length() <= 12)
                .collect(Collectors.joining(", "));

// outputs "Jane Brown, Frankie Chen, Jean Limon"
```

Mill has predicates for these common tasks, allowing fluent
predicate composition. This can be very readable with a couple of static
imports.
```java
// mill
import static com.scottshipp.code.mill.stream.ComparablePredicates.isBetween;
import static com.scottshipp.code.mill.stream.StringPredicates.*;

Predicate<String> maxLen12AndRangeAToM = nonNull().and(nonEmpty())
                .and(isBetween("A", "M"))
                .and(withMaximumLength(12));
String staffAtoMWithShortNames = engineeringTeam
                .filter(maxLen12AndRangeAToM)
                .collect(Collectors.joining(", "));
```

#### Filter out elements in a stream that aren't found in other streams
```java
 // standard Java, one way to do it
 Set<String> distinctItemsInStream2 = stream2.collect(Collectors.toSet());
 Set<String> distinctItemsInStream3 = stream3.collect(Collectors.toSet());
 Set<String> distinctItemsInStream4 = stream4.collect(Collectors.toSet());
 Stream<String> results = stream1.filter(s -> !distinctItemsInStream2.contains(s));
 results.filter(s -> !distinctItemsInStream3.contains(s));
 results.filter(s -> !distinctItemsInStream4.contains(s));

 // mill
 StreamOps.intersection(stream1, stream2, stream3, stream4);
```

#### More Examples
More examples can be found in the API Documentation. 

(To find the API documentation see the Where to find help section below.) 

## Contribution guidelines
Please contribute by [forking the project](https://guides.github.com/activities/forking/) and opening a pull request.

## Where to find help
First, check the API documentation! This is a Maven project so you can always generate the latest API documentation with the javadoc tool as follows:

1. Clone the repo to your local system.
2. Run `mvn javadoc:javadoc`
3. Look in the target/site/apidocs folder.

You can also check the [hosted documentation](http://code.scottshipp.com/mill-javadocs/) at code.scottshipp.com. Important! There is no guarantee that this link has the latest documentation.

If the documentation doesn't answer your question, and you still need general help using mill, you can contact me via [code.scottshipp.com](http://code.scottshipp.com/contact).

If you found an issue with the library, you are welcome to file an issue on [github](https://github.com/scottashipp/mill) or open a merge request.

## License
Mill is made available under the MIT License. See the LICENSE file for more details.

[![ko-fi](https://www.ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/A0A7147N8)
