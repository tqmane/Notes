# 🚀 リリース作成ガイド

## 📋 リリース方法

### 方法1: タグをプッシュして自動リリース（推奨）

**1. バージョンタグを作成**
```bash
# 例: バージョン 1.0.0 をリリース
git tag -a v1.0.0 -m "Version 1.0.0 リリース

主な変更点:
- 筆圧対応描画機能
- OPPO/OnePlus デバイス最適化
- 複数ノート管理
- 自動保存機能"

# タグ一覧を確認
git tag
```

**2. タグをプッシュ**
```bash
git push origin v1.0.0
```

**3. GitHub で確認**
- **Actions** タブで自動的に `Release Build` が開始される
- 完了後、**Releases** タブに新しいリリースが作成される
- APKファイルがダウンロード可能になる

### 方法2: 手動実行（テスト用）

手動実行ではAPKのビルドのみが行われ、Releaseは作成されません。

**1. GitHub で手動実行**
1. リポジトリの **Actions** タブを開く
2. 左サイドバーから **Release Build** を選択
3. **Run workflow** ボタンをクリック
4. ブランチを選択（通常は `main`）
5. **Run workflow** をクリック

**2. APK をダウンロード**
- ワークフロー完了後、**Artifacts** セクションから `release-apk` をダウンロード

## 📝 バージョン番号の規則

セマンティックバージョニング（SemVer）を使用：

```
v[Major].[Minor].[Patch]
```

### バージョン番号の例

| バージョン | 説明 | 使用例 |
|----------|------|--------|
| `v1.0.0` | メジャーリリース | 最初の正式版 |
| `v1.1.0` | マイナーアップデート | 新機能追加 |
| `v1.0.1` | パッチリリース | バグ修正 |
| `v2.0.0` | メジャーアップデート | 破壊的変更 |

### いつバージョンを上げる？

**Major (1.x.x → 2.x.x)**
- 大規模な機能追加
- APIの破壊的変更
- UIの大幅な変更

**Minor (x.1.x → x.2.x)**
- 新機能の追加
- 既存機能の改善
- 後方互換性あり

**Patch (x.x.1 → x.x.2)**
- バグ修正
- 軽微な改善
- セキュリティパッチ

## 🔄 リリースフロー

### 完全なリリースプロセス

```bash
# 1. 最新のコードを取得
git checkout main
git pull origin main

# 2. 変更をコミット（すべての作業が完了している）
git add .
git commit -m "chore: Prepare for v1.0.0 release"

# 3. バージョンタグを作成
git tag -a v1.0.0 -m "Version 1.0.0

新機能:
- 筆圧対応描画
- マルチノート管理
- 自動保存

バグ修正:
- テーマ名のタイプミス修正
- Java 17 対応"

# 4. コミットとタグをプッシュ
git push origin main
git push origin v1.0.0

# 5. GitHub で確認
# Actions タブでビルド状況を確認
# Releases タブでリリースを確認
```

## 📦 リリース成果物

### 自動生成されるファイル

**署名付きAPK（キーストア設定時）:**
```
NotesApp-1.0.0-signed.apk
```

**未署名APK（キーストア未設定時）:**
```
NotesApp-1.0.0-unsigned.apk
```

### Release ページの内容

自動的に以下が含まれます：

```markdown
## NotesApp v1.0.0

### 📦 ビルド情報
✅ 署名付きAPK (または ⚠️ 未署名APK)

### 新機能
- リリースノートをここに記入

### ダウンロード
APKファイルをダウンロードしてインストールしてください。

### 動作環境
- Android 8.0 (API 26) 以上
- OPPO/OnePlus デバイスで最適化

### インストール方法
1. APKファイルをダウンロード
2. デバイスの「設定」>「セキュリティ」で「提供元不明のアプリ」を許可
3. APKファイルをタップしてインストール
```

## 🔧 リリースノートのカスタマイズ

`.github/workflows/release.yml` の `body` セクションを編集：

```yaml
- name: Create Release
  uses: softprops/action-gh-release@v1
  with:
    files: release/*.apk
    body: |
      ## NotesApp v${{ steps.get_version.outputs.VERSION }}
      
      ### 🎉 新機能
      - スタイラス筆圧対応
      - OPPO/OnePlus最適化
      
      ### 🐛 バグ修正
      - テーマ名修正
      
      ### 📥 ダウンロード
      下のAPKファイルをダウンロードしてください
```

## 🚨 トラブルシューティング

### エラー: "GitHub Releases requires a tag"

**原因:** 手動実行時にタグが存在しない

**解決策:**
- タグを作成してプッシュする（方法1）
- または手動実行の場合はArtifactsからAPKをダウンロード

### タグの削除・再作成

```bash
# ローカルのタグを削除
git tag -d v1.0.0

# リモートのタグを削除
git push origin :refs/tags/v1.0.0

# 新しいタグを作成
git tag -a v1.0.0 -m "Updated release"
git push origin v1.0.0
```

### リリースの削除

1. GitHub の **Releases** タブを開く
2. 削除したいリリースの右上の ⋯ をクリック
3. **Delete release** を選択
4. 確認して削除

## 📊 リリース履歴の例

```
v1.0.0 (2025-10-16) - 初回リリース
- 基本的な描画機能
- ノート管理機能

v1.1.0 (2025-11-01) - 機能追加
- Samsung S Pen サポート
- カラーパレット拡張

v1.0.1 (2025-10-20) - バグ修正
- 描画時のクラッシュ修正
- UI改善
```

## ✅ チェックリスト

リリース前に確認：

- [ ] すべての変更がコミット済み
- [ ] ビルドがローカルで成功
- [ ] GitHub Actions のテストがすべて通過
- [ ] バージョン番号が適切
- [ ] CHANGELOG.md を更新（あれば）
- [ ] リリースノートを準備
- [ ] 署名設定を確認（本番リリースの場合）

---

**次のアクション:**

```bash
# 最初のリリースを作成
git tag -a v1.0.0 -m "Version 1.0.0 - 初回リリース"
git push origin v1.0.0
```

🎉 Happy Releasing!

