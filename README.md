# What this branch is

### This is a maven repo for this project. 
Use ```./deploy_release.sh``` on macOS/Linux or ```./deploy_release.ps1``` on Windows to deploy a new release of SmartInvs.
The below tutorial is for if you would like to replicate this yourself. Take care!

--- END ---
## How to use github as a maven repository

In this how-to it is being explained how to create a maven repository on github and how to use an existing one.

## Creating a repository

1. Clone your original project to a new local repository (change GROUP-NAME and PROJECT-NAME) ```git clone https://github.com/GROUP-NAME/PROJECT-NAME.git PROJECT-NAME-maven2```

1. Go to the clonned repository (use your PROJECT-NAME-maven2)
   ```cd PROJECT-NAME-maven2```

1. Create a branch for maven files
   ```git branch maven2```

1. Switch to this new branch
   ```git checkout maven2```

1. Remove project original files, this branch is just for releases
   ```rm -R ALL-PROJECT-SUB-FOLDERS```
   ```rm ALL-PROJECT-FILES```

1. run mvn install for jar creation (change GROUP, ARTIFACT-NAME, ARTIFACT-VERSION, PATH-TO-THE-JAR and PATH-TO-EXISTING-POM). See *1 for details.
   ```mvn install:install-file -DgroupId=GROUP -DartifactId=ARTIFACT-NAME -Dversion=ARTIFACT-VERSION -Dfile=PATH-TO-THE-JAR -Dpackaging=jar -DlocalRepositoryPath=. -DcreateChecksum=true -DgeneratePom=true```

1. Your PATH-TO-THE-JAR will be something like: ```../PROJECT-NAME/build/libs/ARTIFACT-NAME-ARTIFACT-VERSION.jar```. Use ```-DpomFile=PATH-TO-EXISTING-POM``` instead of ```-DgeneratePom=true``` if you already have a POM.

1. Add all files to be commited
   ```git add .```

1. Commit these changes
   ```git commit -m "Released version ARTIFACT-VERSION"```

1. Push this commit. After that your maven structure for you project can be reached by github raw data address https://github.com/GROUP-NAME/PROJECT-NAME/raw/maven2
   ```git push origin maven2```

1. On gradle you can add this repository on 'repositories'
   ```maven { url "https://github.com/ORGANIZATION-NAME/PROJECT-NAME/raw/maven2" }```

1. On maven add this to your repositories:
   ```
   <repository>
      <id>your-id-here-can-be-anything-unique</id>
      <url>https://raw.github.com/<username>/<Project>/maven-repo</url>
    </repository>
   ```
1. Then add this as a dependency:
   ```
   <dependency>
      <groupId>group.id.you.set.in.mvn.install</groupId>
      <artifactId>artifact.id.you.set.in.mvn.install</artifactId>
      <version>version.you.added.in.mvn.install</version>
   </dependency>
   ```

## Using an existing repository

If you already have a repository using this way explained above, you can use the following commands to setup another machine in order to update your repository.

1. Clone your maven2 branch to a local folder which name is followed by "-maven2" (change GROUP-NAME and PROJECT-NAME) ```git clone https://github.com/GROUP-NAME/PROJECT-NAME.git PROJECT-NAME-maven2 --branch maven2```

1. To update your maven2 repo, follow steps from run mvn install for jar creation