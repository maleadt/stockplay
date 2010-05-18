;
; Configuration
;

; Inclusions
!include MUI2.nsh 
 
; Application information
Name    "StockPlay - Backend"       ; The name of the installation
OutFile "StockPlay - Backend (installer).exe"   ; The name of the unistaller file to write

; Default installation directory
InstallDir "$PROGRAMFILES\StockPlay\Backend"

; Compressor settings
SetCompressor /SOLID lzma
SetCompressorDictSize 8

; Pages for installer
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "LICENSE"
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES

; Pages for uninstaller
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

; Supported languages
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "Dutch"

; String localisation declarations
!include "localisation.nsh"

; Global variables
Var TOMCAT


;
; Initialisation
;
 
; At initialization, ABORT if Tomcat has not already been installed
Function .onInit
	; Display language choose dialog
	!insertmacro MUI_LANGDLL_DISPLAY
	
	; Detect Tomcat
	readRegStr $TOMCAT HKLM "SOFTWARE\Apache Software Foundation\Tomcat\6.0" "InstallPath"
	StrCmp $TOMCAT "" 0 Continue  ; IF installation directory string from reg key = null
		MessageBox MB_OK "Tomcat 6.0 is not installed on this system. Installation aborted."
		Abort
	Continue:
		; Do nothing
FunctionEnd


;
; Installation targets
;

; Core
Section "Core (required)" SecCore
	SectionIn RO
	
	SetOutPath "$INSTDIR"
	File "LICENSE"
		
	; Shortcut directory
	CreateDirectory "$SMPROGRAMS\StockPlay\Backend"
	
	; Create an uninstaller
	WriteUninstaller "$INSTDIR\Uninstall.exe"
	CreateShortCut "$SMPROGRAMS\StockPlay\Backend\Uninstall.lnk" "$INSTDIR\Uninstall.exe" "" "$INSTDIR\Uninstall.exe" 0

	; Write the installation path and uninstall keys into the registry
	WriteRegStr HKLM Software\StockPlay_Backend "Install_Dir" $INSTDIR
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\StockPlay_Backend" "UninstallString" '"$INSTDIR\Uninstall.exe"'
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\StockPlay_Backend" "DisplayName" "StockPlay Backend (remove only)"
SectionEnd

; Libraries
Section "Libraries" SecLibs
	SetOutPath "$TOMCAT\lib"
	File "..\..\lib\java\log4j-1.2.16.jar"
	File "..\..\lib\java\xmlrpc-client-3.1.3.jar"
	File "..\..\lib\java\xmlrpc-common-3.1.3.jar"
	File "..\..\lib\java\xmlrpc-server-3.1.3.jar"
	File "..\..\lib\java\commons-dbcp-1.4.jar"
	File "..\..\lib\java\commons-logging-1.1.jar"
	File "..\..\lib\java\commons-pool-1.5.4.jar"
	File "..\..\lib\java\cache4j_0.4.jar"
	File "..\..\lib\java\ojdbc14.jar"
	File "..\..\lib\java\ws-commons-util-1.0.2.jar"
SectionEnd

; Tomcat webapp
Section "Tomcat webservice" SecTomcat
	SetOutPath "$TOMCAT\webapps"
	File "dist\stockplay_backend.war"
SectionEnd

; Source
Section "Source" SecSource
	SetOutPath "$INSTDIR\Source"
	File /r /x .svn "src"
	File /r /x .svn "web"
	
	; Create shortcuts
	createShortCut "$SMPROGRAMS\StockPlay\Backend\View source.lnk" "$INSTDIR\Source"
SectionEnd

; Documentation
Section "Documentation" SecDocs
	SetOutPath "$INSTDIR\Documentation"
	File /r /x .svn "dist\javadoc\*"
	
	; Create shortcuts
	createShortCut "$SMPROGRAMS\StockPlay\Backend\View documentation.lnk" "$INSTDIR\Documentation\index.html"
SectionEnd

; Section descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${SecCore} $(SecCore_Desc)
!insertmacro MUI_DESCRIPTION_TEXT ${SecTomcat} $(SecTomcat_Desc)
!insertmacro MUI_DESCRIPTION_TEXT ${SecSource} $(SecSource_Desc)
!insertmacro MUI_DESCRIPTION_TEXT ${SecDocs} $(SecDocs_Desc)
!insertmacro MUI_DESCRIPTION_TEXT ${SecLibs} $(SecLibs_Desc)
!insertmacro MUI_FUNCTION_DESCRIPTION_END


;
; Uninstall
;
 
; Set prompt text for uninstall window
UninstallText "This will uninstall the StockPlay backend. Press 'Uninstall' to continue."
 
; Define steps to unistall everything installed.
Section "Uninstall"
	; remove registry keys
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\StockPlay_Backend"
	DeleteRegKey HKLM SOFTWARE\StockPlay_Backend
	
	; Source
	RMDir /r "$INSTDIR\Source"
	
	; Documenation
	RMDir /r "$INSTDIR\Documentation"
	
	; Libraries
	Delete "$TOMCAT\lib\log4j-1.2.16.jar"
	Delete "$TOMCAT\lib\xmlrpc-client-3.1.3.jar"
	Delete "$TOMCAT\lib\xmlrpc-common-3.1.3.jar"
	Delete "$TOMCAT\lib\xmlrpc-server-3.1.3.jar"
	Delete "$TOMCAT\lib\commons-dbcp-1.4.jar"
	Delete "$TOMCAT\lib\commons-logging-1.1.jar"
	Delete "$TOMCAT\lib\commons-pool-1.5.4.jar"
	Delete "$TOMCAT\lib\cache4j_0.4.jar"
	Delete "$TOMCAT\lib\ojdbc14.jar"
	Delete "$TOMCAT\lib\ws-commons-util-1.0.2.jar"

	; Core
	Delete "$INSTDIR\Uninstall.exe"
	RMDir "$INSTDIR"
	RMDir /r "$SMPROGRAMS\StockPlay\Backend"            ; remove shortcut directory
	
	; Webapp
	Delete "$TOMCAT\webapps\stockplay_backend.war"
	RMDir /r "$TOMCAT\webapps\stockplay_backend"          ; remove webapp dir IF created by Tomcat
SectionEnd
