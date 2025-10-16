# コントリビューションガイド

NotesApp への貢献に興味を持っていただき、ありがとうございます！

## 🚀 開発環境のセットアップ

1. リポジトリをフォーク
2. クローン: `git clone https://github.com/YOUR_USERNAME/Notes.git`
3. ブランチを作成: `git checkout -b feature/my-feature`
4. Android Studio でプロジェクトを開く

## 📝 プルリクエストの流れ

### 1. コードを書く前に

- Issue を確認し、既に同じ機能の提案や修正がないか確認
- 大きな変更の場合は、事前に Issue で議論

### 2. コーディング

```bash
# 新しいブランチを作成
git checkout -b feature/your-feature-name

# または
git checkout -b fix/bug-description
```

### 3. コミット

コミットメッセージは以下の形式で：

```
[種類] 簡潔な説明

詳細な説明（オプション）

関連 Issue: #123
```

**種類:**
- `feat`: 新機能
- `fix`: バグ修正
- `docs`: ドキュメント更新
- `style`: コードスタイル修正（動作に影響なし）
- `refactor`: リファクタリング
- `test`: テスト追加・修正
- `chore`: ビルド設定など

**例:**
```
feat: Samsung S Pen サポートを追加

- SamsungSPenHandler クラスを実装
- ボタンイベントのハンドリング追加
- エアモーション機能の統合

関連 Issue: #42
```

### 4. プッシュ

```bash
git push origin feature/your-feature-name
```

### 5. プルリクエスト作成

- GitHub でプルリクエストを作成
- テンプレートに従って記入
- CI チェックが通ることを確認

## ✅ コーディング規約

### Java コード

```java
// クラス名: PascalCase
public class StylusHandler {
    
    // 定数: UPPER_SNAKE_CASE
    private static final String TAG = "StylusHandler";
    private static final int MAX_POINTS = 100;
    
    // フィールド: camelCase
    private Context context;
    private boolean isInitialized;
    
    // メソッド: camelCase
    public void initialize(Context context) {
        // 処理
    }
    
    // getter/setter
    public boolean isInitialized() {
        return isInitialized;
    }
}
```

### XML リソース

```xml
<!-- レイアウトID: snake_case -->
<Button
    android:id="@+id/btn_save"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />

<!-- 文字列リソース: snake_case -->
<string name="app_name">NotesApp</string>
<string name="save_note">保存</string>
```

### コメント

```java
/**
 * クラスやメソッドの説明は日本語で
 * 複数行の場合は Javadoc 形式で
 */
public class DrawingView extends View {
    
    // 単一行のコメントも日本語で OK
    private Paint paint;
    
    /**
     * 描画を開始
     * @param point タッチポイント情報
     */
    public void startDrawing(TouchPointInfo point) {
        // 実装
    }
}
```

## 🧪 テスト

新しい機能を追加した場合は、テストも追加してください：

```bash
# ユニットテストを実行
./gradlew test

# すべてのチェックを実行
./gradlew check
```

## 📋 プルリクエストチェックリスト

プルリクエストを作成する前に確認：

- [ ] コードが正しくビルドできる
- [ ] 既存のテストが通る
- [ ] 新機能にはテストを追加した
- [ ] コーディング規約に従っている
- [ ] コメントを適切に記述している
- [ ] README が更新必要な場合は更新した
- [ ] CHANGELOG を更新した（該当する場合）

## 🔍 コードレビュー

- レビュー担当者からのフィードバックに対応
- 建設的な議論を心がける
- コードの品質向上のための提案を歓迎

## 🐛 バグ報告

バグを見つけた場合は Issue を作成してください：

### 必要な情報

1. **環境情報**
   - デバイス: (例: OPPO Find X3 Pro)
   - Android バージョン: (例: Android 13)
   - アプリバージョン: (例: v1.0)

2. **再現手順**
   1. ステップ1
   2. ステップ2
   3. ...

3. **期待される動作**
   - 何が起こるべきか

4. **実際の動作**
   - 何が起こったか

5. **スクリーンショット**（あれば）

6. **ログ**（あれば）
   ```
   adb logcat | grep NotesApp
   ```

## 💡 機能提案

新機能のアイデアがある場合：

1. Issue を作成
2. 以下を含める：
   - 機能の説明
   - なぜ必要か（ユースケース）
   - 可能であれば実装案

## 📞 質問

質問がある場合：

- **一般的な質問**: GitHub Discussions を使用
- **バグ報告**: GitHub Issues を使用
- **セキュリティ問題**: プライベートに報告

## 🙏 行動規範

- 敬意を持って接する
- 建設的なフィードバックを提供
- 多様性を尊重
- オープンでウェルカムなコミュニティを維持

## 📄 ライセンス

コントリビューションは、プロジェクトと同じライセンスの下で公開されます。

---

Happy coding! 🎨✨

