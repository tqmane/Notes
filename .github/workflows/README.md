# GitHub Actions ワークフロー

このディレクトリには、NotesApp の CI/CD ワークフローが含まれています。

## 📋 ワークフロー一覧

### 1. Android CI (`android.yml`)
**トリガー**: `main`, `develop` ブランチへの push/PR

**処理内容**:
- ✅ Gradle ビルド
- ✅ ユニットテスト実行
- ✅ Debug APK 生成
- ✅ Release APK 生成（未署名）
- ✅ APK をアーティファクトとして保存（30日間）

### 2. Run Tests (`test.yml`)
**トリガー**: 
- `main`, `develop` ブランチへの push/PR
- 毎週月曜日 午前9時（UTC）

**処理内容**:
- ✅ ユニットテスト実行
- ✅ Lint チェック
- ✅ テスト結果レポート生成
- ✅ PR にテスト結果をコメント

### 3. Release Build (`release.yml`)
**トリガー**: 
- `v*` タグの push（例: `v1.0.0`）
- 手動実行（workflow_dispatch）

**処理内容**:
- ✅ バージョン番号の抽出
- ✅ build.gradle のバージョン更新
- ✅ キーストアの自動判定
  - **キーストアあり**: 署名付きAPK生成
  - **キーストアなし**: 未署名APK生成
- ✅ GitHub Release の自動作成
- ✅ APK のアップロード

## 🚀 使い方

### ビルドを実行

```bash
# main ブランチにプッシュすると自動実行
git push origin main

# PR を作成すると自動実行
git push origin feature/my-feature
```

### リリースを作成

```bash
# タグを作成してプッシュ
git tag v1.0.0
git push origin v1.0.0

# または手動実行
# GitHub > Actions > Release Build > Run workflow
```

## 🔐 署名設定（オプション）

リリースAPKに署名するには、GitHub Secrets を設定：

### 必要なシークレット

| シークレット名 | 説明 |
|--------------|------|
| `KEYSTORE_FILE` | キーストアファイル（Base64エンコード） |
| `KEYSTORE_PASSWORD` | キーストアのパスワード |
| `KEY_ALIAS` | キーのエイリアス |
| `KEY_PASSWORD` | キーのパスワード |

### 設定手順

1. **キーストアを作成**
   ```bash
   keytool -genkey -v -keystore notesapp.keystore \
     -alias notesapp -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Base64エンコード**
   ```bash
   # Mac/Linux
   base64 -i notesapp.keystore | pbcopy
   
   # Windows (PowerShell)
   [Convert]::ToBase64String([IO.File]::ReadAllBytes("notesapp.keystore"))
   ```

3. **GitHub Secrets に追加**
   - リポジトリの Settings > Secrets and variables > Actions
   - "New repository secret" をクリック
   - 各シークレットを追加

## 📦 成果物のダウンロード

### Actions タブから

1. GitHub リポジトリの **Actions** タブを開く
2. 該当するワークフロー実行をクリック
3. **Artifacts** セクションから APK をダウンロード

### Release から

タグをプッシュした場合、Release ページから APK をダウンロード可能：

- GitHub リポジトリの **Releases** タブ
- 最新リリースの Assets から APK をダウンロード

## 🔧 トラブルシューティング

### ビルドが失敗する

**gradlew の権限エラー**
```yaml
- name: Grant execute permission for gradlew
  run: chmod +x gradlew
```
→ このステップが実行されているか確認

**Gradle キャッシュのクリア**
```bash
# ローカルでビルドしてキャッシュを再生成
./gradlew clean build
git add gradle/
git commit -m "chore: Gradle キャッシュを更新"
```

### 署名が失敗する

**キーストアが正しく設定されているか確認**:
- Secrets が正しく設定されているか
- Base64エンコードが正しいか
- パスワードやエイリアスが正しいか

**未署名APKで代替**:
- キーストア設定を削除すると自動的に未署名APKがビルドされます

### sedコマンドエラー

sedの区切り文字を変更済み（`/` → `|`）：
```bash
sed -i "s|versionName \".*\"|versionName \"${VERSION}\"|" app/build.gradle
```

これにより、パスに `/` が含まれていてもエラーになりません。

## 📊 ワークフロー状態

ビルドステータスバッジ：

```markdown
![Android CI](https://github.com/tqmane/Notes/workflows/Android%20CI/badge.svg)
![Tests](https://github.com/tqmane/Notes/workflows/Run%20Tests/badge.svg)
```

## 🔄 ワークフローのカスタマイズ

### ビルド対象ブランチを追加

```yaml
on:
  push:
    branches: [ main, develop, feature/* ]  # feature/* を追加
```

### テスト実行スケジュールを変更

```yaml
schedule:
  - cron: '0 9 * * 1'  # 毎週月曜日 9:00 UTC
  # - cron: '0 0 * * *'  # 毎日 0:00 UTC
```

### APK保存期間を変更

```yaml
- name: Upload Debug APK
  uses: actions/upload-artifact@v3
  with:
    retention-days: 30  # 30日 → 任意の日数に変更
```

## 📝 ベストプラクティス

1. **タグには必ず `v` プレフィックスを付ける**
   ```bash
   git tag v1.0.0  # ✅ 良い
   git tag 1.0.0   # ❌ 動作しない
   ```

2. **セマンティックバージョニングを使用**
   - `v1.0.0` - メジャーリリース
   - `v1.1.0` - マイナーアップデート
   - `v1.0.1` - パッチ修正

3. **リリースノートを更新**
   - release.yml のリリース本文を編集
   - CHANGELOG.md を作成して管理

---

**更新日**: 2025年10月16日  
**バージョン**: 1.0

