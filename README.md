# Backend

1. Unzip Sap Commerce platform files.


2. Clone repository
   https://gitlab.striped-giraffe.com/sg/davidoff-poc


3. Copy 'platform' and 'modules' folders from extracted folder hybris/bin/
   to cloned project core-customize/hybris/bin/


4. Install extension pack - copy modules and platform folders to core-customize/hybris/bin/
https://help.sap.com/docs/SAP_COMMERCE_INTEGRATIONS/2f43049ad8e443249e1981575adddb5d/8a7b6d2255e6437b804ac9327fa74936.html?locale=en-US&version=2205


5. Change database engine to Mysql:
    - add dependency in external-dependencies.xml ```(core-customize/hybris/bin/platform/lib/dbdriver/external-dependencies.xml)```
    ```
   <dependency>     
         <groupId>com.mysql</groupId>
         <artifactId>mysql-connector-j</artifactId>   
         <version>8.0.32</version>
   </dependency> 
   ```
    - run 'ant updateMavenDependencies' command
    - change db configuration in your local.properties, based on template file - local.properties.template.


6. Set ant using 'setantenv' script. 


7. Initialize platfrom running 'ant initialize' command. 


8. Run server using hybrisserver script.

 
# Frontend

1. Install required libraries:
   - Angular CLI: Version 12.0.5 or later, < 13.
   - Yarn: Version 1.15 or later.
   - Node.js: Version 14.15
   https://sap.github.io/spartacus-docs/building-the-spartacus-storefront-from-libraries-4-x/


2. Move to js-storefront/spartacusstore 
   - install all dependencies running 'yarn install' command
   - run Spartacus using 'yarn start'


3. Application will start on localhost:4200


Test user credentials are
- login: test@dv.com
- password: test123