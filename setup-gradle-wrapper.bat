@echo off
echo Gradle Wrapper のセットアップを開始します...
echo.

REM gradle-wrapper.jar をダウンロード
echo gradle-wrapper.jar をダウンロードしています...
powershell -Command "& {Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle/wrapper/gradle-wrapper.jar'}"

if exist gradle\wrapper\gradle-wrapper.jar (
    echo.
    echo ✓ gradle-wrapper.jar のダウンロードが完了しました
    echo.
    echo セットアップが完了しました！
    echo.
    echo 次のコマンドでビルドできます:
    echo   gradlew.bat build
    echo.
) else (
    echo.
    echo × ダウンロードに失敗しました
    echo.
    echo 手動でセットアップする場合は以下のコマンドを実行してください:
    echo   gradle wrapper
    echo.
)

pause

