# DevDroidAI Sample — メモ帳 (Memopad)

このリポジトリは **DevDroidAI クローズドテスト用のサンプル Android アプリ**です。
シンプルなメモ帳（入力欄からメモを追加 → 一覧表示 → 削除）で、
**テスターが自分のリポジトリを用意しなくても DevDroidAI の基本操作を試せる**ように用意しています。

> This repository is a **sample Android app for DevDroidAI closed testing**.
> A tiny memo pad so testers can try DevDroidAI's basic flow without preparing their own repo.

- 言語: Kotlin / Jetpack Compose / Material 3
- applicationId: `com.e2e.memopad`（本番 DevDroidAI とは無関係の独立サンプル）
- 本番の Firebase / Stripe / Worker などには接続しません。
- 構成: `domain/Memo.kt`（追加・削除の純ロジック）+ `ui/MemoScreen.kt`（入力欄 + 一覧）+ `MainActivity.kt`（状態保持と SharedPreferences 永続化）。

## テスターが試すこと（基本フロー）
1. DevDroidAI アプリにサインイン（GitHub / GitLab）。
2. このリポジトリを追加（**ビルド環境を追加**のチェックはオンのまま）。
3. 自然言語で修正を依頼。
4. 自動で PR が作成され、クラウドビルドが走る。
5. ビルドされた APK を端末にインストールして実機で確認。
6. 「問題なし / 追加修正 / 閉じる / マージ」を選ぶ。

## DevDroidAI で依頼する変更例（コピペで試せる）
- 「メモに追加した日時も表示して」
- 「メモを長押ししたら編集できるようにして」
- 「一覧を新しい順に並べ替えて」
- 「アプリのタイトルを『My Notes』に変更して」
- 「メモが空のときのメッセージをもっと親しみやすくして」

> 小さく具体的な依頼ほど成功しやすいです。大きな変更は分割して依頼してください。

## ⚠️ 注意
- **このリポジトリに secret を追加しないでください**（API キー / 署名鍵 / トークン /
  `google-services.json` / `local.properties` / サービスアカウント等）。public リポジトリです。
- alpha 段階のため不具合が起きることがあります。動作確認用のサンプルとしてご利用ください。
- 重要なコードや本番リポジトリでの利用は避けてください。

## ローカルでビルドする場合
```bash
./gradlew assembleDebug
```
（Android SDK が必要です。`local.properties` は各自のローカルにのみ置き、コミットしないでください。）

<!-- run_log smoke test -->
