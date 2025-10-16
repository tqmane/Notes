# NotesApp インストールガイド

このガイドでは、NotesApp のビルドとインストール方法を詳しく説明します。

## 📋 目次

1. [必要要件](#必要要件)
2. [開発環境のセットアップ](#開発環境のセットアップ)
3. [プロジェクトのビルド](#プロジェクトのビルド)
4. [デバイスへのインストール](#デバイスへのインストール)
5. [トラブルシューティング](#トラブルシューティング)

## 必要要件

### ソフトウェア
- **Android Studio**: Arctic Fox (2020.3.1) 以降
- **JDK**: Java 17 以降（**必須**）
- **Android SDK**: API Level 26 以上
- **Gradle**: 8.0 以降（Android Studio に含まれる）

> ⚠️ **重要**: Android Gradle Plugin 8.2 は Java 17 が必須です。Java 11 では動作しません。

### ハードウェア
- **開発用PC**: 
  - RAM: 8GB 以上推奨
  - ストレージ: 10GB 以上の空き容量
- **テストデバイス**:
  - Android 8.0 (API 26) 以上
  - スタイラス対応デバイス推奨（OPPO/OnePlus）

## 開発環境のセットアップ

### 1. JDK 17 のインストール

**Android Studio を使用する場合:**
- Android Studio には JDK 17 が同梱されています
- Settings > Build, Execution, Deployment > Build Tools > Gradle
- Gradle JDK で「Embedded JDK (17)」を選択

**コマンドラインでビルドする場合:**

**Windows:**
```powershell
# Chocolatey を使用
choco install openjdk17

# または手動で https://adoptium.net/ からダウンロード
```

**Mac:**
```bash
# Homebrew を使用
brew install openjdk@17

# パスを設定
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

**Linux:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**確認:**
```bash
java -version
# openjdk version "17.x.x" と表示されることを確認
```

### 2. Android Studio のインストール

1. [Android Studio 公式サイト](https://developer.android.com/studio)からダウンロード
2. インストーラーを実行
3. セットアップウィザードに従ってインストール
4. 初回起動時に必要なSDKコンポーネントをダウンロード

### 3. Android SDK の設定

Android Studio で以下をインストール：

```
Tools > SDK Manager

【SDK Platforms】
✓ Android 14.0 (API 34)
✓ Android 8.0 (API 26)

【SDK Tools】
✓ Android SDK Build-Tools
✓ Android SDK Platform-Tools
✓ Android Emulator
```

### 4. デバイスの設定

#### 実機を使用する場合

**Android デバイス側:**
1. `設定` > `開発者向けオプション` を開く
   - 開発者向けオプションが表示されない場合:
     - `設定` > `デバイス情報` > `ビルド番号` を7回タップ
2. `USBデバッグ` を有効化
3. PCとUSBケーブルで接続
4. デバッグを許可するか確認ダイアログが表示されたら「許可」

**PC側での確認:**
```bash
# デバイスが認識されているか確認
adb devices

# 出力例
List of devices attached
1234567890ABCDEF    device
```

#### エミュレータを使用する場合

Android Studio で:
1. `Tools` > `Device Manager`
2. `Create Device` をクリック
3. デバイスを選択（例: Pixel 5）
4. システムイメージを選択（API 34 推奨）
5. `Finish` をクリック

## プロジェクトのビルド

### 方法1: Android Studio を使用

1. **プロジェクトを開く**
   ```
   File > Open > NotesApp ディレクトリを選択
   ```

2. **Gradle を同期**
   - 自動的に同期が始まらない場合:
     ```
     File > Sync Project with Gradle Files
     ```

3. **ビルド**
   ```
   Build > Make Project (Ctrl+F9 / Cmd+F9)
   ```

4. **実行**
   ```
   Run > Run 'app' (Shift+F10 / Ctrl+R)
   ```

### 方法2: コマンドラインを使用

```bash
# プロジェクトディレクトリに移動
cd NotesApp

# Windows の場合
gradlew.bat assembleDebug

# Mac/Linux の場合
./gradlew assembleDebug

# APK の場所
# app/build/outputs/apk/debug/app-debug.apk
```

### リリースビルド（署名付き）

```bash
# リリース用 APK を生成
./gradlew assembleRelease

# APK の場所
# app/build/outputs/apk/release/app-release-unsigned.apk
```

## デバイスへのインストール

### 方法1: Android Studio から直接インストール

1. デバイスまたはエミュレータを起動
2. Android Studio で `Run` > `Run 'app'`
3. デバイスを選択
4. 自動的にインストールされて起動

### 方法2: ADB コマンドでインストール

```bash
# APK をインストール
adb install app/build/outputs/apk/debug/app-debug.apk

# すでにインストール済みの場合は -r オプションで上書き
adb install -r app/build/outputs/apk/debug/app-debug.apk

# インストール確認
adb shell pm list packages | grep notesapp

# アプリを起動
adb shell am start -n com.tqmane.notesapp/.MainActivity
```

### 方法3: APK を直接転送

1. **APK をデバイスに転送**
   ```bash
   adb push app/build/outputs/apk/debug/app-debug.apk /sdcard/
   ```

2. **デバイスでファイルマネージャーを開く**

3. **APK ファイルをタップしてインストール**
   - 「提供元不明のアプリ」の許可が必要な場合があります

## トラブルシューティング

### ビルドエラー

#### エラー: "Gradle sync failed"

**解決策:**
```bash
# キャッシュをクリア
./gradlew clean

# 再度ビルド
./gradlew build
```

#### エラー: "SDK location not found"

**解決策:**
1. プロジェクトルートに `local.properties` ファイルを作成
2. 以下を追加（パスは環境に合わせて変更）:
   ```properties
   sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
   # Mac/Linux の場合
   # sdk.dir=/Users/YourName/Library/Android/sdk
   ```

### インストールエラー

#### エラー: "INSTALL_FAILED_UPDATE_INCOMPATIBLE"

**解決策:**
```bash
# 既存のアプリをアンインストール
adb uninstall com.tqmane.notesapp

# 再度インストール
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### エラー: "device unauthorized"

**解決策:**
1. デバイスでUSBデバッグの許可ダイアログを確認
2. 許可していない場合は許可する
3. 許可済みの場合:
   ```bash
   adb kill-server
   adb start-server
   adb devices
   ```

### 実行時エラー

#### アプリがクラッシュする

**確認方法:**
```bash
# ログを確認
adb logcat | grep NotesApp

# または Android Studio の Logcat ウィンドウで確認
```

#### スタイラスが認識されない

**確認事項:**
1. デバイスがスタイラスをサポートしているか
2. AndroidManifest.xml に必要な権限が記載されているか
3. スタイラスのBluetooth接続が有効か（Samsung S Pen など）

## ビルド設定のカスタマイズ

### アプリケーションIDの変更

`app/build.gradle`:
```groovy
android {
    defaultConfig {
        applicationId "com.yourname.notesapp"  // ここを変更
        // ...
    }
}
```

### バージョンの変更

`app/build.gradle`:
```groovy
android {
    defaultConfig {
        versionCode 2
        versionName "1.1"
        // ...
    }
}
```

### 署名設定（リリースビルド用）

1. **キーストアを作成**
   ```bash
   keytool -genkey -v -keystore notesapp.keystore -alias notesapp -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **build.gradle に署名設定を追加**
   ```groovy
   android {
       signingConfigs {
           release {
               storeFile file("notesapp.keystore")
               storePassword "your_password"
               keyAlias "notesapp"
               keyPassword "your_password"
           }
       }
       
       buildTypes {
           release {
               signingConfig signingConfigs.release
               // ...
           }
       }
   }
   ```

## パフォーマンス最適化

### ProGuardの有効化（リリースビルド）

`app/build.gradle`:
```groovy
buildTypes {
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### APKサイズの削減

```groovy
android {
    buildTypes {
        release {
            shrinkResources true
            minifyEnabled true
        }
    }
    
    splits {
        abi {
            enable true
            reset()
            include 'arm64-v8a', 'armeabi-v7a'
            universalApk false
        }
    }
}
```

## 次のステップ

✅ ビルドとインストールが完了したら:
1. [README.md](README.md) で使い方を確認
2. 実際のデバイスでテスト
3. フィードバックや改善提案を Issue で共有

---

問題が解決しない場合は、[GitHub Issues](https://github.com/tqmane/Notes/issues) でお気軽に質問してください！

