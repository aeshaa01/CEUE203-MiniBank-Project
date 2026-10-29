# MiniBank - Java Capstone Project

## Features
- Open accounts
- Deposit and withdraw
- Transfer between accounts
- Account statements
- List accounts
- Generate and save reports
- Validate input and handle banking exceptions
- Generic repository using HashMap and reflection
- Save and load account data using Java serialization
- Thread-safe transaction processing using ExecutorService

## Requirements
- Java JDK 17 or newer

## Compile
From this project folder:

    javac -encoding UTF-8 -d out (Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)

If compiling from PowerShell, use the supplied run script or compile all Java files with javac.

## Run

    java -cp out app.Main

## Run self-tests

    java -cp out test.BankServiceTest

## Data files
- data/accounts.dat stores saved account data.
- data/bank-report.txt stores the generated report.

## Notes
This is an educational console application. It is not intended for real banking use.
