# OOP Project - Part B Models

This project contains the model layer for the university system shown in `project.pdf`.

Part B requires models only, so the project focuses on:

- classes;
- interfaces;
- enums;
- relationships between model objects;
- simple domain methods.

It does not implement a full working simulation or console workflow.

## Structure

- `oopproject.academic` - courses, enrollments, schedule, rooms, requests, reports, news.
- `oopproject.users` - user hierarchy: admin, student, teacher, manager, employee.
- `oopproject.research` - researcher interface, research papers, research projects.
- `oopproject.enums` - all enums from the UML draft.
- `oopproject.exceptions` - custom exceptions.
- `oopproject.core` - university singleton, auth service, data store, logs, UI controller placeholder.

## Compile

```powershell
javac -d out (Get-ChildItem -Recurse src/main/java/*.java).FullName
```

## Run

```powershell
java -cp out oopproject.Main
```
