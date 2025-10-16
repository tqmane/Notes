# NotesApp - OPPO Stylus API 対応ノートアプリ

OPPO/OnePlus の VFX SDK を使用した手書きノートアプリです。筆圧検知、モーション予測などの高度な機能を搭載しています。

## 🎨 機能

### ✏️ 描画機能
- **筆圧対応**: スタイラスの筆圧を検知し、線の太さを動的に調整
- **モーション予測**: OPPO VFX SDK による高度な軌跡予測で滑らかな描画
- **カラーパレット**: 8色のカラー選択
- **線の太さ調整**: 細い、普通、太い、極太の4段階
- **Undo機能**: 最後の筆跡を取り消し
- **クリア機能**: すべての描画を消去

### 📒 ノート管理
- **複数ノート対応**: 無制限にノートを作成
- **自動保存**: バックグラウンド移行時やアプリ終了時に自動保存
- **サムネイル表示**: ノート一覧にサムネイル画像を表示
- **タイトル編集**: ノート名を自由に変更可能
- **削除機能**: 不要なノートを削除

### 🎯 マルチデバイス対応
- **OPPO/OnePlus**: VFX SDK による高度な予測とタッチ処理
- **汎用デバイス**: すべてのAndroidデバイスで動作（フォールバック機能）
- **自動検出**: デバイスに最適なハンドラーを自動選択

## 📁 プロジェクト構造

```
NotesApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/tqmane/notesapp/
│   │   │   ├── MainActivity.java              # メイン画面
│   │   │   ├── DrawingActivity.java           # 描画画面
│   │   │   ├── stylus/                        # スタイラスAPI統合
│   │   │   │   ├── TouchPointInfo.java        # タッチポイント情報
│   │   │   │   ├── StylusHandler.java         # ハンドラーインターフェース
│   │   │   │   ├── OppoStylusHandler.java     # OPPO/OnePlus用
│   │   │   │   ├── GenericStylusHandler.java  # 汎用ハンドラー
│   │   │   │   └── UnifiedStylusManager.java  # 統合マネージャー
│   │   │   ├── views/
│   │   │   │   └── DrawingView.java           # カスタム描画ビュー
│   │   │   ├── models/
│   │   │   │   └── Note.java                  # ノートデータモデル
│   │   │   ├── storage/
│   │   │   │   └── NoteStorage.java           # ストレージ管理
│   │   │   └── adapters/
│   │   │       └── NotesAdapter.java          # RecyclerViewアダプター
│   │   ├── res/
│   │   │   ├── layout/                        # レイアウトXML
│   │   │   ├── values/                        # 色、文字列、テーマ
│   │   │   ├── menu/                          # メニュー定義
│   │   │   └── drawable/                      # アイコンリソース
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md (このファイル)
```

## 🚀 セットアップ方法

### 必要要件
- Android Studio Arctic Fox 以降
- **JDK 17** 以降（重要！）
- Android SDK 26 (Android 8.0) 以上
- Gradle 8.0 以降

### ビルド手順

1. **リポジトリをクローン**
```bash
git clone https://github.com/tqmane/Notes.git
cd Notes
```

2. **Android Studioで開く**
- Android Studio を起動
- "Open an Existing Project" を選択
- クローンしたディレクトリを選択

3. **Gradleの同期**
- Android Studio が自動的に依存関係をダウンロード
- エラーが出た場合は "File > Sync Project with Gradle Files" を実行

4. **Gradle Wrapper のセットアップ**

GitHub Actions でビルドするには、`gradle-wrapper.jar` が必要です。

**自動セットアップ（Windows）:**
```batch
setup-gradle-wrapper.bat
```

**手動セットアップ:**
```bash
# Windows (PowerShell)
Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle/wrapper/gradle-wrapper.jar'

# Mac/Linux
curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar
```

**確認:**
```bash
.\gradlew.bat --version  # Windows
./gradlew --version      # Mac/Linux
```

詳細は [SETUP_GRADLE.md](SETUP_GRADLE.md) を参照してください。

5. **ビルド＆実行**
```bash
# コマンドラインから
.\gradlew.bat assembleDebug  # Windows
./gradlew assembleDebug      # Mac/Linux

# または Android Studio で
Run > Run 'app'
```

## 🤖 GitHub Actions による自動ビルド

このプロジェクトは GitHub Actions を使用した CI/CD を設定済みです。

### 自動実行されるワークフロー

#### 1. Android CI (`.github/workflows/android.yml`)
- **トリガー**: `main`, `develop` ブランチへの push/PR
- **処理内容**:
  - ビルド実行
  - ユニットテスト実行
  - Debug/Release APK 生成
  - APK をアーティファクトとしてアップロード

#### 2. テスト (`.github/workflows/test.yml`)
- **トリガー**: push/PR、毎週月曜日
- **処理内容**:
  - ユニットテスト実行
  - Lint チェック
  - テスト結果のレポート生成

#### 3. リリース (`.github/workflows/release.yml`)
- **トリガー**: `v*` タグの push
- **処理内容**:
  - リリース APK のビルド
  - APK への署名（キーストア設定時のみ）
  - 未署名APKもサポート（キーストア未設定時）
  - GitHub Release の自動作成

### ビルドバッジの追加

README にビルドステータスバッジを追加できます：

```markdown
![Android CI](https://github.com/tqmane/Notes/workflows/Android%20CI/badge.svg)
```

### APK のダウンロード

ビルドが成功すると、GitHub の Actions タブから APK をダウンロードできます：

1. GitHub リポジトリの `Actions` タブを開く
2. 最新のワークフロー実行を選択
3. `Artifacts` セクションから APK をダウンロード

### リリース署名の設定（オプション）

**✨ 署名なしでもリリース可能です！** キーストアが未設定の場合、自動的に未署名APKがビルドされます。

#### 署名付きAPKを作成する場合

GitHub Secrets を設定することで、署名付きAPKを自動生成できます：

**1. キーストアを作成**（まだない場合）
```bash
keytool -genkey -v -keystore notesapp.keystore \
  -alias notesapp -keyalg RSA -keysize 2048 -validity 10000
```

**2. GitHub Secrets を設定**
- GitHub リポジトリの **Settings > Secrets and variables > Actions**
- 以下のシークレットを追加：
  - `KEYSTORE_FILE`: キーストアファイル（Base64エンコード）
  - `KEYSTORE_PASSWORD`: キーストアのパスワード
  - `KEY_ALIAS`: キーのエイリアス
  - `KEY_PASSWORD`: キーのパスワード

**3. キーストアをBase64エンコード**
```bash
# Mac
base64 -i notesapp.keystore | pbcopy

# Linux
base64 -w 0 notesapp.keystore

# Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("notesapp.keystore"))
```

#### APKファイル名の違い

- **署名付き**: `NotesApp-1.0.0-signed.apk` ✅
- **未署名**: `NotesApp-1.0.0-unsigned.apk` ⚠️

## 📱 使い方

### ノートの作成
1. メイン画面右下の **+ボタン** をタップ
2. 自動的に新規ノートが作成され、描画画面が開きます

### 描画
- **スタイラスまたは指** で画面をタッチして描画
- 筆圧が自動検知され、線の太さが変化します
- **色ボタン**: 描画色を変更
- **太さボタン**: 線の太さを変更
- **Undoボタン**: 最後の筆跡を取り消し
- **クリアボタン**: すべての描画を消去

### ノートの管理
- **タップ**: ノートを開く
- **長押し**: ノートを削除
- **メニュー > 名前変更**: ノートのタイトルを変更
- **メニュー > 保存**: 手動で保存（自動保存もあり）

## 🔧 技術仕様

### 使用技術
- **言語**: Java
- **最小SDK**: Android 8.0 (API 26)
- **ターゲットSDK**: Android 14 (API 34)
- **ビルドツール**: Gradle 8.2.0

### 主要ライブラリ
- **AndroidX**: AppCompat, RecyclerView, CardView
- **Material Components**: 1.11.0
- **Gson**: 2.10.1 (JSON処理)

### アーキテクチャ
- **MVC パターン**
  - Model: `Note.java`
  - View: `DrawingView.java`, レイアウトXML
  - Controller: `MainActivity.java`, `DrawingActivity.java`

### データ保存
- **ノートメタデータ**: JSON形式で内部ストレージに保存
- **描画画像**: PNG形式で内部ストレージに保存
- **パス**: `/data/data/com.tqmane.notesapp/files/`

## 🎯 OPPO Stylus API について

このアプリは [tqmane/oppo-stylus-api](https://github.com/tqmane/oppo-stylus-api) リポジトリで解析された OPPO/OnePlus VFX SDK を参考に実装されています。

### 主な機能
1. **TouchPointInfo**: タッチポイントの座標、筆圧、傾き情報
2. **MotionPredictor**: 高度な軌跡予測アルゴリズム
3. **デバイス自動検出**: 最適なAPIを自動選択

### サポートデバイス
- ✅ **OPPO/OnePlus**: 高度な予測機能
- ✅ **汎用Androidデバイス**: 標準タッチ処理

## 📝 開発ロードマップ

### v1.0 (現在) ✅
- [x] 基本的な描画機能
- [x] 筆圧対応
- [x] ノート管理（作成、保存、削除）
- [x] OPPO/OnePlus対応

### v1.1 (予定)
- [ ] Samsung S Pen 対応
- [ ] Huawei/Honor スタイラス対応
- [ ] エクスポート機能（PNG, PDF）
- [ ] 複数ページ対応

### v2.0 (予定)
- [ ] クラウド同期
- [ ] 図形認識機能
- [ ] テキスト挿入
- [ ] レイヤー機能

## 🐛 既知の問題

現在、既知の重大な問題はありません。バグを発見した場合は Issue を作成してください。

## 📄 ライセンス

このプロジェクトは学習・研究目的で作成されました。

### ⚠️ 重要な注意事項
- OPPO VFX SDK のネイティブライブラリは含まれていません
- 商用利用の場合は、OPPO から正式な SDK を入手してください
- HiPaint アプリのリバースエンジニアリング結果を参考にしています

## 🤝 コントリビューション

プルリクエストを歓迎します！大きな変更の場合は、まず Issue を開いて変更内容を議論してください。

## 📧 お問い合わせ

質問やフィードバックがあれば、GitHub Issues でお気軽にご連絡ください。

---

**開発者**: tqmane  
**バージョン**: 1.0  
**最終更新**: 2025年10月16日

🎨 OPPO Stylus API で、快適な手書きノート体験を！
