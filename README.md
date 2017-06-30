# Reset-Password-Spring-boot


This spring boot app demonstrate "Forgot my password" flow.

After submitting the form an email will be sent to the mail address the user provided and an access token will be generated and will be kept in a map regestry with the user object.

In this email he will be redirected to the reset HTML page . The access token will be added to the HTML "Reset your password" as a quiry parameter for uthentication.

When the user will try to rest the password, the "isEligible" method will try to authenticate the access token he came with.
When authenticated, the new password will be hashed with SHY-2 algorithm and updeted in the database.

SPRING BOOT - FORGOT MY PASSWORD APP :

#SPRING BOOT, JPArepository #Embedded H2 #MAVEN #SHY-2 #JAVAMail #AngularJs #HTML

*Please note that the javaMail method in Util.class comes with default gmail configuration, u will need to confingure it to your satisfaction and include your own Email and Username.

To run the application run : http://localhost:8090

@Any questions you can send to orenhoffman1777@gmail.com
