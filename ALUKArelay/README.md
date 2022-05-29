# Aluka relay
[![N|Solid](https://www.hornetsecurity.com/wp-content/uploads/2021/10/Ransomware-Attacks-Survey-Q3-Main-Blog-Image.jpg)](https://nodesource.com/products/nsolid)
## Concept 
>Aluka relay is playing intermediate role, it should be hosted on a server a part, when the ransomware is started as POWERED mode, these intermediate software will provide tor service(without browser) that will allow the Virus to send you back the Key of the encryption
## Installation
> The following code is the source code of the relay, follow these steps to make your own compile version
```diff
- Please make sure that maven is installed at your variable environnement
```
```sh
    cd ALUKArelay
    mvn clean compile assembly:single
```
### => You will find the compiled version on the target/ALUKcd /ALUKArelay-1.0-SNAPSHOT-jar-with-dependencies.jar
