# Team Workflow

Use `oopproject.core.University` as the integration point. Team members should not create separate global lists in their own packages.

## Package Ownership

- `users`: user hierarchy and user-specific actions.
- `academic`: courses, enrollments, lessons, marks, schedules, reports.
- `research`: researchers, papers, projects, research sorting and statistics.
- `core`: registration, lookup, authentication, logs, persistence, console entrypoint.

## How To Integrate A Feature

1. Create or update the model in the package that owns it.
2. Add the object to `University` through an existing method, for example `addUser`, `addCourse`, `addResearcher`, or `addProject`.
3. If another package needs the object, use lookup methods from `University`, for example `findUserById`, `findCourseByCode`, or `findProjectByTopic`.
4. Keep business rules in the owning package. Example: research h-index rules stay in `research`/`Student`, not in `ConsoleUIController`.
5. Keep console printing in `ConsoleUIController`; model classes should return data, not control the app flow.

## Git Rules

- One task per branch.
- Pull before starting work.
- Do not commit `out/`, `.class`, `.ser`, or local `data/` files.
- Before pushing, run:

```powershell
javac -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
```

## Recommended Split

- Person 1: `users` and authentication.
- Person 2: `academic` course registration, marks, transcript.
- Person 3: `research` papers, projects, h-index, comparators.
- Person 4: `core`, persistence, console demo, final integration.
