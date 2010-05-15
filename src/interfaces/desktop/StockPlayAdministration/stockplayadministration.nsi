!include "MUI2.nsh"


; The name of the installer
Name "StockPlay Administration"

; The file to write
OutFile "Installer.exe"

; The default installation directory
InstallDir "$PROGRAMFILES\StockPlay Administration"

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\StockPlay_Administration" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel admin
;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING


;--------------------------------

; Pages

  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_LICENSE "dist\gpl.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !insertmacro MUI_UNPAGE_FINISH

  ;--------------------------------
;Languages

  !insertmacro MUI_LANGUAGE "English"
  !insertmacro MUI_LANGUAGE "Dutch"


;--------------------------------

; The stuff to install
Section "StockPlay Administration (required)"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File /r "dist\*.*"
  
  ; Write the installation path into the registry
  WriteRegStr HKLM "Software\StockPlay_Administration" "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\StockPlay_Administration" "DisplayName" "StockPlay Administration client"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\StockPlay_Administration" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\StockPlay_Administration" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\StockPlay_Administration" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

  CreateDirectory "$SMPROGRAMS\StockPlay Administration"
  CreateShortCut "$SMPROGRAMS\StockPlay Administration\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\StockPlay Administration\StockPlay Administration.lnk" "javaw" "-jar StockPlayAdministration.jar" "$INSTDIR\money_safe.ico" 0
  
SectionEnd

Section "Desktop Shortcut"

  CreateShortCut "$DESKTOP\StockPlay Administration.lnk" "javaw" "-jar StockPlayAdministration.jar" "$INSTDIR\money_safe.ico" 0

SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\StockPlay_Administration"
  ;DeleteRegKey HKLM SOFTWARE\NSIS_Example2

  ; Remove files and uninstaller
	Delete "$INSTDIR\uninstall.exe"
	Delete "$INSTDIR\*.*"


  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\StockPlay Administration\*.*"

  ; Remove directories used
  RMDir "$SMPROGRAMS\StockPlay Administration"
  RMDir /r /REBOOTOK "$INSTDIR"

SectionEnd
