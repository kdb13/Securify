
# Specification

## Development

Our development worstation will have the following specification:

* #### Software Specification 
	* **IDE**: Android Studio v3.5
	* **Language**: Java, XML
	* **Platform**: Microsoft Windows 10
	* **Backend Service**: Google Firebase

* #### Hardware Specification
	* **Processor**: Intel i5-8th Gen
	* **RAM**: 4GB or more
	* **Testing Equipment**: Android Smartphone

## End-User

The end-user must have an Android smartphone with following specifications:

* **Platform**: Android Lollipop (API 21 or later)
* GPS (Location)

<br>

# Overview of Language

**Securify** will be developed using the native Android SDK. It is the most native way of developing Android applications. We are targetting the Android devices as most of the people are using an Android smartphone right now.

We will use _Java_ and _XML_ to develop the application.

* #### Java
	* Java will be used to deisgn the logic structure of the whole Android application.
	* There will be a global _Application_ class which contains various Android components like 
		1. Activities (Visible UI)
		2. Services (Background Tasks)
		3. Broadcast Receivers
		4. Content Providers
	* By writing the Java code, the app will interact with the various UI elements which statically defined in **XML**.

* #### XML
	* XML will provide the static UI (User Interface) written in separate **_.xml_** files.
	* These designed will be stored as separate resources will be a part of the Android pacakge.

Most of the implementation will be done using the core Android SDK, along with some additional open-source libraries for licensing. We will use the latest Android SDK available (currently **_API 28_**) for development, as it provides new features and more secure applications.

# Scope

**Securify** will provide the following features:

* By pressing a sequence of volume buttons, we can:
	* Send an alert without opening SMS or dialer app
	* Call an ICE responder or the contact
	* Start video or voice recording
* A dialog box will be shown to perform all operations, without opening the app
* Upto 5 contacts can be selected which will be notified through alert
* The current location of the victim will also be sent
* The volunteer can easily send an acknowledgement or a customized reply again from a simple dialog box.  