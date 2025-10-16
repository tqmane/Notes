# 📱 APK インストールガイド

## ❌ 「パッケージが無効の可能性があります」エラー

### 原因

このエラーは通常、以下の理由で発生します：

1. **未署名APK** - リリースAPKが署名されていない
2. **デバッグキーの問題** - デバッグAPKの署名が信頼されていない
3. **デバイスの制限** - セキュリティ設定でブロックされている
4. **互換性の問題** - デバイスのAndroidバージョンが古い

## ✅ 解決方法

### 方法1: デバッグAPKを使用（推奨・最も簡単）

デバッグAPKは自動的に署名されるため、すぐにインストールできます。

**GitHub Actionsから取得:**

1. GitHub リポジトリの **Actions** タブを開く
2. **Android CI** ワークフローを選択
3. 最新の成功したビルドをクリック
4. **Artifacts** セクションから `app-debug` をダウンロード
5. ZIPを解凍して `app-debug.apk` を取得

**インストール手順:**
```bash
# デバイスに転送
adb push app-debug.apk /sdcard/

# インストール
adb install app-debug.apk
```

または：
1. APKをデバイスに転送
2. ファイルマネージャーでAPKをタップ
3. インストールを許可

### 方法2: リリースAPKに署名する

#### A. キーストアを作成

```bash
keytool -genkey -v -keystore notesapp.keystore \
  -alias notesapp \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

**入力情報:**
- パスワード: 安全なパスワードを設定
- 名前: あなたの名前
- 組織: 組織名（個人の場合は名前でOK）
- 市区町村、都道府県、国コード: 入力

#### B. GitHub Secrets に設定

1. **キーストアをBase64エンコード**

**Windows (PowerShell):**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("notesapp.keystore")) | Set-Clipboard
```

**Mac/Linux:**
```bash
base64 -i notesapp.keystore | pbcopy  # Mac
base64 -w 0 notesapp.keystore         # Linux
```

2. **GitHub に追加**
   - リポジトリの **Settings** > **Secrets and variables** > **Actions**
   - 以下のシークレットを追加:

| Name | Value |
|------|-------|
| `KEYSTORE_FILE` | Base64エンコードしたキーストア |
| `KEYSTORE_PASSWORD` | キーストアのパスワード |
| `KEY_ALIAS` | `notesapp` |
| `KEY_PASSWORD` | キーのパスワード |

3. **リリースを作成**
```bash
git tag v1.0.0
git push origin v1.0.0
```

→ 署名付きAPKが自動生成されます

#### C. ローカルで署名

```bash
# 1. 未署名APKをビルド
./gradlew assembleRelease

# 2. 署名
jarsigner -verbose \
  -sigalg SHA256withRSA \
  -digestalg SHA-256 \
  -keystore notesapp.keystore \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  notesapp

# 3. zipalignで最適化
zipalign -v 4 \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  app/build/outputs/apk/release/app-release-signed.apk

# 4. 署名を確認
jarsigner -verify -verbose -certs \
  app/build/outputs/apk/release/app-release-signed.apk
```

### 方法3: デバイス設定を確認

#### Android 8.0以降の場合

1. **提供元不明のアプリを許可**
   - 設定 > アプリ > 特別なアプリアクセス > 提供元不明のアプリ
   - ファイルマネージャーを選択
   - 「この提供元を許可」をオンにする

#### OPPOデバイスの場合

1. **設定** > **その他の設定** > **デバイスとプライバシー**
2. **提供元不明のアプリ** > ファイルマネージャーを許可

#### Xiaomiデバイスの場合

1. **設定** > **プライバシー保護** > **特別な権限**
2. **提供元不明のアプリのインストール** > ファイルマネージャーを許可

## 🔍 トラブルシューティング

### エラー: "アプリはインストールされていません"

**原因:** APKが破損しているか、不完全

**解決策:**
1. APKを再ダウンロード
2. ダウンロード中に中断されていないか確認
3. ファイルサイズを確認（数MB以上あるはず）

### エラー: "パッケージの解析中に問題が発生しました"

**原因:** APKの形式が不正、またはデバイスと互換性がない

**解決策:**
1. Androidバージョンを確認（Android 8.0以上が必要）
   ```bash
   adb shell getprop ro.build.version.release
   ```
2. CPUアーキテクチャを確認
   ```bash
   adb shell getprop ro.product.cpu.abi
   ```
   - arm64-v8a または armeabi-v7a が必要

### エラー: "署名の競合"

**原因:** 同じパッケージ名の異なる署名のアプリが既にインストールされている

**解決策:**
1. 既存のアプリをアンインストール
   ```bash
   adb uninstall com.tqmane.notesapp
   ```
2. 新しいAPKをインストール

## 📋 推奨手順

### 開発中（テスト用）

```bash
# 1. デバッグAPKをビルド
./gradlew assembleDebug

# 2. インストール
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 配布用（一般ユーザー向け）

1. キーストアを作成（一度だけ）
2. GitHub Secretsに設定（一度だけ）
3. タグをプッシュしてリリース
4. 署名付きAPKを配布

## ⚙️ build.gradle に署名設定を追加（オプション）

ローカルで常に署名付きAPKをビルドする場合：

```gradle
android {
    signingConfigs {
        release {
            storeFile file("../notesapp.keystore")
            storePassword System.getenv("KEYSTORE_PASSWORD") ?: "your_password"
            keyAlias "notesapp"
            keyPassword System.getenv("KEY_PASSWORD") ?: "your_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

**環境変数を設定:**
```bash
# Windows (PowerShell)
$env:KEYSTORE_PASSWORD = "your_password"
$env:KEY_PASSWORD = "your_password"

# Mac/Linux
export KEYSTORE_PASSWORD="your_password"
export KEY_PASSWORD="your_password"
```

## 🎯 まとめ

### すぐに試したい場合
→ **デバッグAPK** を使用（GitHub Actions の Artifacts からダウンロード）

### 配布したい場合
→ **キーストアを作成** して署名付きAPKをビルド

### エラーが続く場合
1. Androidバージョンを確認（8.0以上）
2. デバイス設定で「提供元不明のアプリ」を許可
3. 既存のアプリをアンインストールしてから再インストール

---

**それでも解決しない場合:**
- APKファイルのサイズとSHA256ハッシュを確認
- `adb logcat` でエラーログを確認
- GitHub Issues で詳細を報告


