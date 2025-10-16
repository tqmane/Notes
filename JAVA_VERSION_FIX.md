# 🔧 Java バージョンエラーの修正

## ❌ エラー内容
```
Android Gradle plugin requires Java 17 to run. You are currently using Java 11.
```

## ✅ 解決済み

すべてのワークフローファイルを **Java 17** に更新しました！

### 修正されたファイル

1. ✅ `.github/workflows/android.yml` - Java 11 → 17
2. ✅ `.github/workflows/test.yml` - Java 11 → 17
3. ✅ `.github/workflows/release.yml` - Java 11 → 17
4. ✅ `app/build.gradle` - compileOptions を Java 17 に更新

## 📋 次のステップ

### 1. 変更をコミット＆プッシュ

```bash
# すべての変更を追加
git add .

# コミット
git commit -m "fix: Update to Java 17 for Android Gradle Plugin compatibility

- GitHub Actions ワークフローを Java 17 に更新
- build.gradle の compileOptions を Java 17 に更新
- ドキュメントに Java 17 要件を追記"

# プッシュ
git push origin main
```

### 2. GitHub Actions で確認

- GitHub リポジトリの **Actions** タブを開く
- 最新のワークフローが **成功** することを確認 ✅

## 🖥️ ローカル開発環境の設定

### Android Studio を使用する場合

1. **Settings** (File > Settings / Preferences)
2. **Build, Execution, Deployment > Build Tools > Gradle**
3. **Gradle JDK** で以下を選択:
   - `Embedded JDK (17)` または
   - システムにインストールした JDK 17

### コマンドラインでビルドする場合

**Java 17 のインストール:**

**Windows (Chocolatey):**
```powershell
choco install openjdk17
```

**Mac (Homebrew):**
```bash
brew install openjdk@17
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**確認:**
```bash
java -version
# openjdk version "17.x.x" が表示されればOK
```

## 📝 なぜ Java 17 が必要？

Android Gradle Plugin 8.0 以降は、以下の理由で Java 17 が必須です:

| Gradle Plugin Version | 必要な Java バージョン |
|----------------------|-------------------|
| AGP 7.x | Java 11 以降 |
| **AGP 8.x** | **Java 17 以降** ✅ |

このプロジェクトは AGP 8.2 を使用しているため、Java 17 が必須です。

## 🔍 トラブルシューティング

### ローカルでビルドエラーが出る場合

**1. Java バージョンを確認:**
```bash
java -version
javac -version
```

**2. JAVA_HOME を設定:**

**Windows (PowerShell):**
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"
```

**Mac/Linux:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
# または
export JAVA_HOME=$(/usr/libexec/java_home -v 17)  # Mac
```

**3. gradle.properties に追加:**
```properties
org.gradle.java.home=/path/to/jdk-17
```

### GitHub Actions でまだエラーが出る場合

ワークフローファイルを確認:
```yaml
- name: Set up JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'  # ← 17 になっているか確認
    distribution: 'temurin'
```

## 🎉 完了

これで Java 17 の要件を満たし、ビルドが成功するはずです！

---

**更新日**: 2025年10月16日  
**対応バージョン**: Android Gradle Plugin 8.2+

