mill
====

Java library to make run-of-the-mill tasks more elegant. 

Compatible with Java 8+.

## In this README
* Some examples
* Contribution guidelines
* Where to find help
* License

## Some examples

The following are just a few of the things you'll find in mill.

#### Fluent interface for null conditional operations
If you have a chain of calls into an object graph (such as `user.getEmail().getDomain().length()`) and you care about avoiding a NullPointerException, you might write code like this:

```java
    int length = 0;
    if(user != null) {
        Email userEmail = user.getEmail();
        if(userEmail != null ) {
            String domain = userEmail.getDomain();
            if(domain != null) {
                length = domain.length();
            }
        }
    }
```

With the `NullSafe` class, you can convert the above to:

```java
int length = NullSafe.of(user)
                     .call(User::getEmail)
                     .call(Email::getDomain)
                     .call(String::length)
                     .getOrDefault(0));
```

#### Join a stream of non-string objects together into a string
```java
Stream<Holiday> majorUsHolidays = Stream.of(newYears, easter, independenceDay, thanksgiving, christmas);

// standard Java requires us to first map Object::toString
majorUsHolidays.map(Object::toString).collect(Collectors.joining(", "));

// mill doesn't require the extra map call
majorUsHolidays.collect(CustomCollectors.joining(", "));
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
import static com.scottshipp.code.mill.Strings.*;

Predicate<String> maxLen12AndRangeAToM = nonNull().and(nonEmpty()).and(inRange("A", "M")).and(withMaximumLength(12));
String staffAtoMWithShortNames = engineeringTeam.filter(maxLen12AndRangeAToM).collect(Collectors.joining(", "));
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

## Contribution guidelines
Please contribute by [forking the project](https://guides.github.com/activities/forking/) and opening a pull request.

## Where to find help
First, check the javadoc! This is a Maven project so you can find the
javadoc by cloning the repo to your local system, running `mvn javadoc:javadoc`,
and then looking in the target/site folder.

If you still need general help using mill, you can contact me via [code.scottshipp.com](http://code.scottshipp.com/contact).

If you found an issue, you are welcome to file an issue on [github](https://github.com/scottashipp/WordPlay) or open a merge request.

## License
Mill is made available under the MIT License. See the LICENSE file for more details.
