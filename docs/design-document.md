# devdroidai-e2e-onboarding 設計書

## プロジェクト概要

devdroidai-e2e-onboarding は、DevDroid AI のオンボーディング用 E2E テスト対象として開発された Kotlin/Compose ベースのメモ帳アプリケーションです。シンプルなメモの追加・削除機能を備え、SharedPreferences を用いた永続化を実装しています。

対象 Android バージョン：minSdk 26、targetSdk 36
開発言語：Kotlin
UI フレームワーク：Jetpack Compose

## アーキテクチャ概要

本アプリケーションは、ビジネスロジック層、UI 層、永続化層の 3 層構造を採用しています。各層は明確に分離されており、unit test 可能な設計になっています。

### 層別責務

ビジネスロジック層（domain パッケージ）：メモの追加・削除ロジックを担当。Android フレームワークに依存しないため、JVM 上で直接テスト可能です。

UI 層（ui パッケージ）：Compose を用いた画面描画とユーザー入力処理を担当。Material Design 3 をベースとしています。

永続化層（MainActivity 内）：SharedPreferences を用いた JSON ベースの永続化実装。メモリスト全体を JSON 配列として保存します。

## アーキテクチャ図

```
┌─────────────────────────────────────────────┐
│         Jetpack Compose UI Layer            │
│  ┌──────────────────────────────────────┐   │
│  │     MemoScreen（画面描画）            │   │
│  │  - メモリスト表示                     │   │
│  │  - 入力フィールド                     │   │
│  │  - 追加・削除ボタン                   │   │
│  └──────────────────────────────────────┘   │
└──────────────┬───────────────────────────────┘
               │ イベントコールバック
               ↓
┌──────────────────────────────────────────────┐
│       MainActivity（状態管理・制御）          │
│  ┌──────────────────────────────────────┐    │
│  │  Compose State Holder                │    │
│  │  - memos: List<Memo>                 │    │
│  │  - input: String                     │    │
│  └──────────────────────────────────────┘    │
│                                              │
│  ┌──────────────────────────────────────┐    │
│  │  Event Handlers                      │    │
│  │  - onAdd() → MemoLogic.add()         │    │
│  │  - onDelete() → MemoLogic.remove()   │    │
│  └──────────────────────────────────────┘    │
└──────────────┬─────────────────┬─────────────┘
               │                 │
               ↓                 ↓
    ┌─────────────────┐  ┌──────────────┐
    │  MemoLogic      │  │  MemoStore   │
    │ （ビジネス      │  │（永続化層）  │
    │  ロジック層）    │  │              │
    │                 │  │SharedPref    │
    │ immutable       │  │← JSON        │
    │ Pure Function   │  │              │
    └─────────────────┘  └──────────────┘
               ↑                 ↑
               └─────────────────┘
            Memo data model
```

## 主要クラス・ファイルの説明

### domain/Memo.kt

Memo データクラス：id（一意の識別子）、text（メモ本文）、createdAt（作成日時）を保持する不変データ構造です。

MemoLogic オブジェクト：純粋関数を提供する業務ロジック層です。

add 関数：既存のメモリストに新しいメモを追加します。入力テキストが空白のみの場合は追加を行わず元のリストを返します。すべてのメモは trim 処理後のテキストで保存されます。

remove 関数：指定された id のメモを除いた新しいリストを返します。該当メモが存在しない場合は元のリストと同じ内容のリストを返します。

両関数とも immutable 設計であり、入力リストを変更せず新しいリストを返すため、副作用がなく、テスト検証が容易です。

### MainActivity.kt

メモ帳アプリケーションのメインアクティビティです。Compose ベースの UI 構築と状態管理、イベントハンドリングを統括します。

onCreate メソッド：MemoStore を初期化し、保存済みのメモリストをロードします。Compose の remember による mutableState でメモリストと入力フィールドの状態を管理します。MemoScreen コンポーザブルに memos、input、各種イベントコールバックを渡します。

onAdd コールバック：MemoLogic.add() を呼び出してビジネスロジックでメモを追加し、結果を状態に反映します。その後 MemoStore.save() で永続化し、入力フィールドをリセットします。

onDelete コールバック：MemoLogic.remove() でメモを削除し、MemoStore.save() で永続化します。

nextId メソッド：既存メモの最大 id に 1 を加えたものを新しい id として返します。同一リスト内での衝突を防ぐシンプルな採番方式です。

MemoStore 内部クラス：SharedPreferences を用いたメモの永続化を管理します。

load メソッド：SharedPreferences から JSON 配列形式で取得したメモリストをパースして List<Memo> に変換します。JSON パース失敗時は空リストを返します。

save メソッド：メモリストを JSONArray に変換し、SharedPreferences に保存します。各メモは id、text、createdAt を含む JSONObject として格納されます。

### ui/MemoScreen.kt

Jetpack Compose により実装されたメモ帳画面です。メモリスト、入力フィールド、操作ボタンから構成されます。詳細な UI 実装は別ファイルですが、以下の構成要素を持ちます：

メモリスト表示：LazyColumn で memos を縦スクロール表示します。各メモ行には削除ボタン（×）が付属しています。

入力フィールド：TextField でテキスト入力を受け付け、onInputChange コールバックで親の状態を更新します。

追加ボタン：onAdd コールバックを実行し、新しいメモを追加します。

### ui/theme パッケージ

Color.kt、Theme.kt、Type.kt よりアプリケーション全体の色彩スキーム、テーマ、タイポグラフィを定義します。Material Design 3 ガイドラインに準拠した設計です。

## データフロー

ユーザーがメモを追加する場合のデータフロー：

1. ユーザーが入力フィールドに文字を入力
2. onInputChange コールバックが呼び出され、input 状態が更新
3. 追加ボタンを押下
4. onAdd コールバックが実行される
5. MemoLogic.add() を呼び出し、ビジネスロジックでメモ追加処理を実行
6. 返却された新しいメモリストで memos 状態を更新
7. MemoScreen が新しい memos 状態を受け取り、UI 再描画
8. MemoStore.save() で新しいメモリストを SharedPreferences に永続化
9. input 状態をリセット

ユーザーがメモを削除する場合のデータフロー：

1. メモ行の削除ボタン（×）を押下
2. onDelete(id) コールバックが実行される
3. MemoLogic.remove(id) を呼び出し、対象 id を除いたメモリストを取得
4. 返却されたメモリストで memos 状態を更新
5. MemoScreen が新しい memos 状態を受け取り、UI 再描画
6. MemoStore.save() で新しいメモリストを SharedPreferences に永続化

アプリケーション起動時のデータフロー：

1. MainActivity の onCreate が実行される
2. MemoStore が SharedPreferences から既存メモを JSON パース形式で取得
3. load() メソッドで List<Memo> に変換
4. remember { mutableStateOf(store.load()) } で初期状態として設定
5. MemoScreen が初期メモリストと共にコンポーズ

## 依存関係構成

ビルド設定は Gradle Kotlin DSL（build.gradle.kts）により統一されています。

主要な依存関係：

androidx.compose.ui：UI コンポーネント基盤
androidx.compose.material3：Material Design 3 コンポーネント
androidx.compose.runtime：Compose ランタイム、状態管理
androidx.activity:activity-compose：Activity と Compose の統合
androidx.lifecycle:lifecycle-viewmodel-compose：ViewModel 連携
androidx.core:core-splashscreen：スプラッシュスクリーン
org.jetbrains.kotlinx:kotlinx-coroutines-android：非同期処理

テスト用依存関係：

junit：unit test フレームワーク
androidx.test.ext:junit：AndroidX test extension
androidx.test.espresso:espresso-core：UI テスト

## テスト戦略

ExampleUnitTest.kt（unit test）：MemoLogic の add および remove 関数に対する unit test。Android フレームワークに依存しないため、開発マシン上で高速に実行できます。テストケースは以下を網羅しています：

add_appendsTrimmedMemo：トリミング処理を含むメモ追加動作の検証
add_ignoresBlankInput：空白のみの入力に対する無視動作の検証
add_isImmutable：入力リストの不変性を確保する検証
remove_dropsMatchingId：指定 id メモの削除動作の検証
remove_noMatchKeepsAll：該当メモなしの場合の動作検証

ExampleInstrumentedTest.kt（instrumented test）：Android 環境でのアプリケーションコンテキスト検証。エミュレータまたは実機上で実行されます。

## 最近の変更履歴

2026-07-05 93139cf - DevDroid AI ビルドワークフロー追加

2026-07-05 2f45243 - AI 修正：MemoLogic および Memo.kt 関連の修正（PR #7）

2026-07-05 1df5a87 - DevDroid AI ビルドワークフロー追加

2026-07-05 8d2d9da - Memo.kt、MainActivity.kt、ExampleUnitTest.kt の AI 修正（PR #4）

2026-07-05 2c1601e - DevDroid AI ビルドワークフロー追加

2026-07-05 a760f29 - AI 修正：タイトル未指定の修正タスク実行（PR #2）

2026-07-05 333a8ce - DevDroid AI ビルドワークフロー追加

2026-07-05 23a585b - chore: メモ帳サンプルアプリケーションのシード実装（初期コミット）

これらのコミット履歴より、本リポジトリが DevDroid AI のオンボーディング E2E テスト対象として段階的に構築・改善されていることが明らかです。AI による自動修正が複数回適用され、ビルドワークフロー統合も実施されています。

## 拡張性と今後の改善ポイント

本設計の拡張性について以下の点が挙げられます：

ビジネスロジックの独立性：MemoLogic が Android に依存しないため、共通ライブラリ化やマルチプラットフォーム対応が容易です。

永続化層の抽象化：現在 SharedPreferences ベースですが、Room データベース、DataStore、Cloud Firestore などへの置き換えが容易な構造です。

状態管理の拡張：将来的には ViewModel、Kotlin Flow、Redux パターンの導入により、より複雑な状態管理に対応できます。

メモ検索・編集機能：ビジネスロジック層にフィルタリング・更新ロジックを追加することで容易に実装可能です。

オフライン・オンライン同期：永続化層を拡張することで、クラウド同期機能を実装できます。