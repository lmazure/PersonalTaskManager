## Description

Yet another playground to
- evaluate VSCode's Java support
- discover GitHub Actions
- implement an Event Sourcing (https://martinfowler.com/eaaDev/EventSourcing.html) architecture  
  in particular, see how to handle backward compatibility (I need that, I want to eat my dog food)
- perform continuous deployment (hum… this one will be the most tricky)

These goals will evolve as I move forward (if I am able to move forward, I doubt I will have a lot of time to work on this).

What I do not know yet:
- Can I use a Git repo to store the transactions?
- Is the performance high enough to replay the full log at startup?
- Where will is deploy this?
- What JS lib I will use for the UI?

------

## Usage

Build and run
```bash
cd manager
mvn clean package
cd src/main/webapp
tsc
cd ../../..
java -jar target/manager-1.0.jar
```


Test on URL
- http://localhost:8080/index.html
