# OOP Project - Part B Models

This project contains the model layer for the university system shown in `project.pdf`.

Part B requires models only, so the project focuses on:

- classes;
- interfaces;
- enums;
- relationships between model objects;
- simple domain methods.

It does not implement a full working simulation or console workflow.

Research package now covers the important requirements:

- paper sorting by publication date, citations, and article length;
- `printPapers(Comparator<ResearchPaper>)` for each researcher;
- printing all university papers through `ResearchService`;
- top-cited researcher by school or by year;
- h-index validation for fourth-year student supervisors;
- custom exception when a non-researcher tries to join a research project.

## Structure

- `oopproject.academic` - courses, enrollments, schedule, rooms, requests, reports, news.
- `oopproject.academic.Transcript` - student transcript summary with GPA, completed credits and academic standing.
- `oopproject.users` - user hierarchy: admin, student, teacher, manager, employee.
- `oopproject.research` - researcher interface, researcher profiles, research papers, projects, comparators and research service methods.
- `oopproject.enums` - all enums from the UML draft.
- `oopproject.exceptions` - custom exceptions.
- `oopproject.core` - university singleton, auth service, data store, logs, UI controller placeholder.

Core package now works as the integration layer:

- `University` stores users, courses, researchers, research projects and logs;
- `University` coordinates course registration, teacher-course assignment, marks, lookups and course status;
- `AuthService` handles login/logout and writes log records;
- `DataStore` saves/loads the application state with Java serialization and exports a readable JSON snapshot;
- `ConsoleUIController` provides a console demo for student, teacher, manager and admin flows.

## Compile

```powershell
javac -d out (Get-ChildItem -Recurse src/main/java/*.java).FullName
```

## Run

```powershell
java -cp out oopproject.Main
```

Console demo:

```powershell
java -cp out oopproject.Main --console
```
