# misanthrope
Are you sick of social media? Opt out by blocking everyone.

## Usage

This isn't user friendly and hasn't been tested (it's a joke application). 
Using it in a serious capacity will probably lead to your account being blocked for constantly
meeting API rate limits. 

That said, if you would really like to give it a go, here's what you need to do:

* Check out this repository
* Update `twitter4j.properties` inside of the resources folder to hold your API credentials
  (you'll need to make some if you haven't already done so).
* Run `mvn clean install` and run the resultant JAR. 
  Doing so could take an inordinate amount of time[1].
* ???
* _Profit!_ You have now blocked the entire world (in theory).

## Footnotes

[1] There are about 300 million users on twitter. The rate limits are in the hundreds
    of operations every 15 minutes. You do the math on this one.
