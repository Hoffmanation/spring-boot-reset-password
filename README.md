# Reset-Password-Spring-boot


This spring boot app demonstrate "Forgot my password" flow.

After submitting the form an email will be sent to the mail address the user provided, an access token will be generated and will be kept in a map registry with the user object, but for only 5 minutes for security purposes.

In this email he will be redirected to the reset HTML page . The access token will be added to the HTML "Reset your password" as a query parameter for authentication.

When the user will try to rest the password, the "isEligible" method will try to authenticate the access token he came with.
When authenticated (token lifetime still valid and its the same token), the new password will be hashed with SHY-2 algorithm and updeted in the database.

SPRING BOOT - FORGOT MY PASSWORD APP :

#SPRING BOOT, JPArepository 
#Embedded H2 
#MAVEN 
#SHY-2 
#JAVAMail 
#AngularJs 
#HTML

*PLEASE NOTE --- > that the javaMail method in Util.class comes with default gmail configuration, u will need to configure it to your satisfaction and include your own Email and Username.

To run the application run : http://localhost:8090

@For any questions you can send a mail to orenhoffman1777@gmail.com
