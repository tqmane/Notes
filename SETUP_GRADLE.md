# Gradle Wrapper セットアップガイド

GitHub Actions で `GradleWrapperMain` エラーが発生する場合の対処法です。

## 🐛 問題

```
Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain
Caused by: java.lang.ClassNotFoundException: org.gradle.wrapper.GradleWrapperMain
```

このエラーは `gradle/wrapper/gradle-wrapper.jar` ファイルが存在しないために発生します。

## ✅ 解決方法

### 方法1: 自動セットアップ（Windows）

`setup-gradle-wrapper.bat` を実行：

```batch
setup-gradle-wrapper.bat
```

### 方法2: 手動ダウンロード

**Windows (PowerShell):**
```powershell
Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle/wrapper/gradle-wrapper.jar'
```

**Mac/Linux:**
```bash
curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar
```

### 方法3: Gradle コマンドで生成（Gradleインストール済みの場合）

```bash
gradle wrapper
```

## 📋 確認方法

Gradle Wrapper が正しく動作するか確認：

```bash
# Windows
.\gradlew.bat --version

# Mac/Linux
./gradlew --version
```

出力例：
```
------------------------------------------------------------
Gradle 8.2
------------------------------------------------------------
```

## 🔧 .gitignore の設定

`gradle-wrapper.jar` が Git で追跡されるように `.gitignore` を更新済み：

```gitignore
# Gradle files
.gradle/
build/

# Gradle Wrapper (必要なので除外しない)
!gradle/wrapper/gradle-wrapper.jar
```

## 📦 必要なファイル

以下のファイルが必要です：

```
gradle/
└── wrapper/
    ├── gradle-wrapper.jar        # ✅ 必須！
    └── gradle-wrapper.properties # ✅ 必須！
gradlew                           # ✅ 必須！
gradlew.bat                       # ✅ 必須！
```

## 🚀 GitHub Actions での使用

ファイルをコミット＆プッシュ：

```bash
# gradle-wrapper.jar を追加
git add gradle/wrapper/gradle-wrapper.jar
git add .gitignore

# コミット
git commit -m "fix: Add gradle-wrapper.jar for GitHub Actions"

# プッシュ
git push origin main
```

これで GitHub Actions のビルドが成功します！

## 🔍 トラブルシューティング

### ファイルサイズを確認

```bash
# Windows
dir gradle\wrapper\gradle-wrapper.jar

# Mac/Linux
ls -lh gradle/wrapper/gradle-wrapper.jar
```

正常なサイズ: 約 45-60 KB

### 再ダウンロード

ファイルが壊れている場合：

```bash
# 削除
rm gradle/wrapper/gradle-wrapper.jar

# 再ダウンロード（上記の方法を使用）
```

### Gradle バージョンを確認

`gradle/wrapper/gradle-wrapper.properties`:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.2-bin.zip
```

## 📝 参考情報

- [Gradle Wrapper 公式ドキュメント](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
- [GitHub Actions で Android をビルド](https://github.com/actions/setup-java)

---

**更新日**: 2025年10月16日

