# isis-module-security #

[![Build Status](https://travis-ci.org/isisaddons/isis-module-security.png?branch=master)](https://travis-ci.org/isisaddons/isis-module-security)

This module, intended for use within [Apache Isis](http://isis.apache.org), provides the ability to manage *user*s, *role*s,
and *permission*s.  Users have roles, roles have permissions, and permissions are associated with *application feature*s. 
These features are derived from the Isis metamodel and can be scoped at either a _package_, _class_ or individual _class member_.
Permissions themselves can either _allow_ or _veto_ the ability to _view_ or _change_ any application feature.

A key design objective of this module has been to limit the amount of permissioning data required.  To this objective:

* permissions are hierarchical: a class-level permission applies to all class members, while a package-level permission 
  applies to all classes of all subpackages
  
* permissions can _allow_ or _veto_ access; thus a role can be granted access to most features, but excluded from selective others

* permissions are scoped: a member-level permission overrides a class-level permission, a class-level permission 
  overrides a package-level permission; the lower-level package permission overrides a higher-level one 
  (eg `com.mycompany.invoicing` overrides `com.mycompany`).

* if there are conflicting permissions at the same scope, then the _allow_ takes precedence over the __veto__.
  
The module also provides an implementation of [Apache Shiro](http://shiro.apache.org)'s 
[AuthorizingRealm](https://shiro.apache.org/static/1.2.2/apidocs/org/apache/shiro/realm/AuthorizingRealm.html).  This 
allows the users/permissions to be used for Isis' authentication and/or authorization.  If using for authentication,
passwords are encrypted using a `PasswordEncryptionService`.  The module provides a default implementation based on
[jBCrypt](http://www.mindrot.org/projects/jBCrypt/), but other implementations can be plugged-in if required.
  
## Domain Model ##

![](https://raw.github.com/isisaddons/isis-module-security/master/images/domain-model.png)

The above diagram was generated by [yuml.me](http://yuml.me); see appendix at end of page for the DSL.

## Screenshots ##

The following screenshots show an example app's usage of the module, which includes all the services and entities 
([users, roles, permissions](https://github.com/isisaddons/isis-module-security/tree/master/dom/src/main/java/org/isisaddons/module/security/dom) etc) 
provided by the module itself.  This example app's [domain](https://github.com/isisaddons/isis-module-security/tree/master/fixture/src/main/java/org/isisaddons/module/security/fixture/dom) 
also has its own very simple `ExampleEntity` entity and corresponding repository.

For further screenshots, see the [screenshot tutorial](https://github.com/isisaddons/isis-module-security/wiki/Screenshot-Tutorial) on the wiki.

#### Automatically Seeds Roles ####

When the security module starts up, it will automatically seed a number of roles, corresponding permissions and a 
default 'admin' user.  The `isis-module-security-admin` role grants all permissions to all classes in the security module itself:

![](https://raw.github.com/isisaddons/isis-module-security/master/images/030-role.png)

The `isis-module-security-regular-user` role grants selected permissions to the `ApplicationUser` class:

![](https://raw.github.com/isisaddons/isis-module-security/master/images/035-role-regular-user.png)

#### Add permission for all features in a package ####

Permissions created at the package level apply to all classes in all packages and subpackages (that is, recursively).

![](https://raw.github.com/isisaddons/isis-module-security/master/images/040-role-add-permission-package.png)

#### Permissions can ALLOW or VETO access ####

Permissions can either grant (allow) access or prevent (veto) access.  If a user has permissions that contradict each 
other (for example, they are a member of "roleA" that allows the permission, but also of "roleB" that vetoes the
permission) then by default the allow wins.  However, this is strategy is pluggable, and the security module can be 
configured such that a veto would override an allow if required.

![](https://raw.github.com/isisaddons/isis-module-security/master/images/050-permission-rule.png)

#### Permissions can apply to VIEWING or CHANGING the feature ####

For a property, "changing" means being able to edit it.  For a collection, "changing" means being able to add or remove
from it.  For an action, "changing" means being able to invoke it.

![](https://raw.github.com/isisaddons/isis-module-security/master/images/060-permission-mode.png)

Note that Isis' Wicket viewer currently does not support the concept of "changing" collections; the work-around is 
instead create a pair of actions to add/remove instead.  This level of control is usually needed anyway.

An _allow/changing_ permission naturally enough implies _allow/viewing_, while conversely and symmetrically
 _veto/viewing_ permission implies _veto/changing_.

#### Specify package ####

The list of packages is derived from Isis' own metamodel.

![](https://raw.github.com/isisaddons/isis-module-security/master/images/070-permission-package-from-isis-metamodel.png)

#### Add permission for all members in a class ####

Permissions defined at the class level take precedence to those defined at the package level.  For example, a user
might have _allow/viewing_ at a parent level, but have this escalated to _allow/changing_ for a particular
class.  Conversely, the class-level permission might veto access.

![](https://raw.github.com/isisaddons/isis-module-security/master/images/090-role-add-permission-class.png)

#### Add permission to an individual action of a member ####

Permissions can also be defined at the member level: action, property or collection.  These override permissions 
defined at either the class- or package-level.

For example, to add a permission for an individual action:

![](https://raw.github.com/isisaddons/isis-module-security/master/images/110-role-add-permission-action.png)

#### Application feature for a class member ####

Class members (action, property or collection) lists the permissions defined against that member:

![](https://raw.github.com/isisaddons/isis-module-security/master/images/280-feature.png)

It provides access in turn to the parent (class) feature... 

#### Application feature for a class ####

The class feature lists associated permissions (if any), also the child properties, collections and actions:

![](https://raw.github.com/isisaddons/isis-module-security/master/images/283-class-feature.png)

It also provides access to its parent (package) feature ...

#### Application feature for a package ####

The package feature lists its associated permissions (if any), its contents (class or package features) and also 
provides access to its parent (package) feature.

![](https://raw.github.com/isisaddons/isis-module-security/master/images/286-package-feature.png)

#### Application users ####

The security module can be defined either as a primary Shiro realm (for both authentication and authorization) or a 
secondary realm (for authorization only).

If configured as a primary realm, then the users must be created by the administrator.    If configured as a secondary
realm, then the user is created automatically, sychronized with the primary realm.

Once the user is created, then additional information about that user can be captured, including their name and
contact details.  This information is not otherwise used by the security module, but may be of use to other parts
of the application.  The users' roles and effective permissions are also shown.
 
![](https://raw.github.com/isisaddons/isis-module-security/master/images/289-user-details.png)

A user can maintain their own details, but may not alter other users' details.  An administrator can alter all details,
as well as reset a users' password.

If a user is disabled, then they may not log in.  This is useful for temporarily barring access to users without 
having to change all their roles, for example if they leave the company or go on maternity leave.


## How to configure/use ##

You can either use this module "out-of-the-box", or you can fork this repo and extend to your own requirements. 

### Out-of-the-box Configuration ###

#### Shiro configuration (shiro.ini) ####

The module includes `org.isisaddons.module.security.shiro.IsisModuleSecurityRealm`, an implementation of Apache Shiro's
 `org.apache.shiro.realm.AuthorizingRealm` class. As such, the module can be used to be used either as the single 
 (primary) realm or as a secondary realm.  

* if configured as the primary realm then module handles both authentication and authorization.  Authentication is 
  performed against encrypted password.

* if configured as the secondary realm then it primarily handles authorization.

In either configuration the module can also prevent a user logging on if that user's account has been disabled. 

For both cases, update your `WEB-INF/shiro.ini`'s `[main]` section:

<pre>
[main]

isisModuleSecurityRealm=org.isisaddons.module.security.shiro.IsisModuleSecurityRealm

authenticationStrategy=org.isisaddons.module.security.shiro.AuthenticationStrategyForIsisModuleSecurityRealm
securityManager.authenticator.authenticationStrategy = $authenticationStrategy

</pre>

The, set up Shiro`s `securityManager.realms` property.  To use the module as the primary realm (with passwords visible
and verified), add (again, in the `[main]` section):

<pre>
securityManager.realms = $isisModuleSecurityRealm
</pre>

Or, to use the module as the secondary realm (without password support), add:

<pre>
securityManager.realms = $someOtherRealm,$isisModuleSecurityRealm
</pre>


where `$someOtherRealm` refers to the definition of the realm to perform primary authentication (eg `$iniRealm`, or an
`$ldapRealm` implementation.


#### Isis domain services (isis.properties) ####

Update the `WEB-INF/isis.properties`:

<pre>
    isis.services-installer=configuration-and-annotation
    isis.services.ServicesInstallerFromAnnotation.packagePrefix=
            ...,\
            org.isisaddons.module.security,\
            ...

    isis.services = ...,\
            org.isisaddons.module.security.dom.password.PasswordEncryptionServiceUsingJBcrypt,\
            org.isisaddons.module.security.app.user.MeService,\
            org.isisaddons.module.security.dom.permission.PermissionsEvaluationServiceAllowBeatsVeto,\
            ...
</pre>

where:

* the `PasswordEncryptionServiceUsingJBcrypt` is an implementation of the `PasswordEncryptionService`.  This is 
  mandatory if the module is configured as a primary realm.  If required, any other implementation can be suppled.

* The `MeService` provides the ability for an end-user to lookup their own user account, if granted the 
  `isis-module-security-regular-user` role.  This service is optional, no other functionality in the module depends
  on this service.

* The `PermissionsEvaluationServiceAllowBeatsVeto` is an implementation of the `PermissionsEvaluationService` that 
  determines how to resolve conflicting permissions at the same scope.  This service is optional; if not present
  then the module will default to an allow-beats-veto strategy.  An alternative implementation of
  `PermissionsEvaluationServiceVetoBeatsAllow` is also available for use if required; or any other implementation
   of this interface can be supplied.

There is further discussion of the `PasswordEncryptionService` and `PermissionsEvaluationService` below.


#### Classpath ####

Finally, update your classpath by adding this dependency in your dom project's `pom.xml`:

<pre>
    &lt;dependency&gt;
        &lt;groupId&gt;org.isisaddons.module.security&lt;/groupId&gt;
        &lt;artifactId&gt;isis-module-security-dom&lt;/artifactId&gt;
        &lt;version&gt;1.6.0&lt;/version&gt;
    &lt;/dependency&gt;
</pre>

If using the `PasswordEncryptionServiceUsingJBcrypt` service, also add a dependency on the underlying library:

<pre>
    &lt;dependency&gt;
        &lt;groupId&gt;org.mindrot&lt;/groupId&gt;
        &lt;artifactId&gt;jbcrypt&lt;/artifactId&gt;
        &lt;version&gt;0.3m&lt;/version&gt;
    &lt;/dependency&gt;
</pre>

Check for later releases by searching [Maven Central Repo](http://search.maven.org/#search|ga|1|isis-module-security-dom).


### Extending the Module ###

If instead you want to extend this module's functionality, then we recommend that you fork this repo.  The repo is 
structured as follows:

* `pom.xml   ` - parent pom
* `dom       ` - the module implementation, depends on Isis applib
* `fixture   ` - fixtures, holding a sample domain objects and fixture scripts; depends on `dom`
* `integtests` - integration tests for the module; depends on `fixture`
* `webapp    ` - demo webapp (see above screenshots); depends on `dom` and `fixture`

Only the `dom` project is released to Maven Central Repo.  The versions of the other modules are purposely left at 
`0.0.1-SNAPSHOT` because they are not intended to be released.


## API and Implementation ##

The module defines a number of services and default implementations.  The behaviour of the module can be adjusted
by implementing and registerng alternative implementations.

### PasswordEncryptionService ###

The `PasswordEncryptionService` (used only when the module is configured as a primary realm) is responsible for 
performing a one-way encryption of password to encrypted form.  This encrypted version is then stored in the 
`ApplicationUser` entity's `encryptedPassword` property.

The service defines the following API:

<pre>
public interface PasswordEncryptionService {
    public String encrypt(final String password);
    public boolean matches(final String candidate, final String encrypted);
}
</pre>

The `PasswordEncryptionServiceUsingJbcrypt` provides an implementation of this service based on Blowfish algorithm.  It
depends in turn on `org.mindrot:jbcrypt` library; see above for details of updating the classpath to reference this
library.


### PermissionsEvaluationService ###

The `PermissionsEvaluationService` is responsible for determining which of a number of possibly conflicting permissions
apply to a target member.  It defines the following API:
 
<pre>
public interface PermissionsEvaluationService {
    public ApplicationPermissionValueSet.Evaluation evaluate(
                final ApplicationFeatureId targetMemberId,
                final ApplicationPermissionMode mode,
                final Collection<ApplicationPermissionValue> permissionValues);
</pre>

It is _not_ necessary to register any implementation of this service in `isis.properties`; by default a strategy of
allow-beats-veto is applied.  However this strategy can be explicitly specified by registering the (provided)
`PermissionsEvaluationServiceAllowBeatsVeto` implementation, or alternatively it can be reversed by registering 
`PermissionsEvaluationServiceVetoBeatsAllow`.  Of course some other implementation with a different algorithm may 
instead be registered.


## Default Roles, Permissions and Users ###

Whenever the application starts the security module checks for (and creates if missing) the following roles, permissions
and users: 

* `isis-module-security-admin` role
    * _allow_ _changing_ of all classes (recursively) under the `org.isisaddons.module.security.app` package 
    * _allow_ _changing_ of all classes (recursively) under the `org.isisaddons.module.security.dom` package 
* `isis-module-security-regular-user` role
    * _allow_ _changing_ (ie invocation) of the `org.isisaddons.module.security.app.user.MeService#me` action
    * _allow_ _viewing_ of the `org.isisaddons.module.security.app.dom.ApplicationUser` class
    * _allow_ _changing_ of the selected "self-service" actions of the `org.isisaddons.module.security.app.dom.ApplicationUser` class
* `isis-module-security-fixture` role
    * _allow_ _changing_ of `org.isisaddons.module.security.fixture` package (run example fixtures if prototyping) 
* `admin` user
    * granted `isis-module-security-admin` role
* `isis-applib-fixtureresults` role
    * _allow_ _changing_ of `org.apache.isis.applib.fixturescripts.FixtureResult` class

This work is performed by the `SeedSecurityModuleService`.


## Future Directions/Possible Improvements ##

The module currently does not support:
- setting permissions on the root package.  Instead, must specify for `org` or `com` top-level package.  This means that
  the edge case of permissions for a class in the root package is not supported.
- users could possibly be extended to include user settings, refactored out from [isis-module-settings](https://github.com/isisaddons/isis-module-settings)
- features could possibly be refactored out/merged with [isis-module-devutils](https://github.com/isisaddons/isis-module-devutils). 
- hierarchical roles
- tenancy applied to roles (so only apply permissions of a user's global roles or those of their tenancy; requires a
  custom override of Isis' AuthorizationManagerStandard so can pass through the tenancy of the target object being
  evaluated.


## Legal Stuff ##
 
#### License ####

    Copyright 2014 Dan Haywood

    Licensed under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.


#### Dependencies ####

In addition to Apache Isis, this module depends on:

* `org.mindrot:jbcrypt` (Apache-like license)


##  Maven deploy notes ##

Only the `dom` module is deployed, and is done so using Sonatype's OSS support (see 
[user guide](http://central.sonatype.org/pages/apache-maven.html)).

#### Release to Sonatype's Snapshot Repo ####

To deploy a snapshot, use:

    pushd dom
    mvn clean deploy
    popd

The artifacts should be available in Sonatype's 
[Snapshot Repo](https://oss.sonatype.org/content/repositories/snapshots).

#### Release to Maven Central (scripted process) ####

The `release.sh` script automates the release process.  It performs the following:

* perform sanity check (`mvn clean install -o`) that everything builds ok
* bump the `pom.xml` to a specified release version, and tag
* perform a double check (`mvn clean install -o`) that everything still builds ok
* release the code using `mvn clean deploy`
* bump the `pom.xml` to a specified release version

For example:

    sh release.sh 1.6.1 \
                  1.6.2-SNAPSHOT \
                  dan@haywood-associates.co.uk \
                  "this is not really my passphrase"
    
where
* `$1` is the release version
* `$2` is the snapshot version
* `$3` is the email of the secret key (`~/.gnupg/secring.gpg`) to use for signing
* `$4` is the corresponding passphrase for that secret key.

If the script completes successfully, then push changes:

    git push
    
If the script fails to complete, then identify the cause, perform a `git reset --hard` to start over and fix the issue
before trying again.

#### Release to Maven Central (manual process) ####

If you don't want to use `release.sh`, then the steps can be performed manually.

To start, call `bumpver.sh` to bump up to the release version, eg:

     `sh bumpver.sh 1.6.1`

which:
* edit the parent `pom.xml`, to change `${isis-module-command.version}` to version
* edit the `dom` module's pom.xml version
* commit the changes
* if a SNAPSHOT, then tag

Next, do a quick sanity check:

    mvn clean install -o
    
All being well, then release from the `dom` module:

    pushd dom
    mvn clean deploy -P release \
        -Dpgp.secretkey=keyring:id=dan@haywood-associates.co.uk \
        -Dpgp.passphrase="literal:this is not really my passphrase"
    popd

where (for example):
* "dan@haywood-associates.co.uk" is the email of the secret key (`~/.gnupg/secring.gpg`) to use for signing
* the pass phrase is as specified as a literal

Other ways of specifying the key and passphrase are available, see the `pgp-maven-plugin`'s 
[documentation](http://kohsuke.org/pgp-maven-plugin/secretkey.html)).

If (in the `dom`'s `pom.xml` the `nexus-staging-maven-plugin` has the `autoReleaseAfterClose` setting set to `true`,
then the above command will automatically stage, close and the release the repo.  Sync'ing to Maven Central should 
happen automatically.  According to Sonatype's guide, it takes about 10 minutes to sync, but up to 2 hours to update 
[search](http://search.maven.org).

If instead the `autoReleaseAfterClose` setting is set to `false`, then the repo will require manually closing and 
releasing either by logging onto the [Sonatype's OSS staging repo](https://oss.sonatype.org) or alternatively by 
releasing from the command line using `mvn nexus-staging:release`.

Finally, don't forget to update the release to next snapshot, eg:

    sh bumpver.sh 1.6.2-SNAPSHOT

and then push changes.

## Appendix: yuml.me DSL

<pre>
[ApplicationUser|username{bg:green}]0..*->0..1[ApplicationTenancy|name{bg:blue}]
[ApplicationUser]1-0..*>[ApplicationRole|name{bg:yellow}]
[ApplicationRole]1-0..*>[ApplicationPermission]
[ApplicationFeature|fullyQualifiedName{bg:green}]-memberType>0..1[ApplicationMemberType|PROPERTY;COLLECTION;ACTION]
[ApplicationFeature]->type[ApplicationFeatureType|PACKAGE;CLASS;MEMBER]
[ApplicationPermission{bg:pink}]++->[ApplicationFeature]
[ApplicationPermission]->[ApplicationPermissionMode|VIEWING;CHANGING]
[ApplicationPermission]->[ApplicationPermissionRule|ALLOW;VETO]
</pre>

