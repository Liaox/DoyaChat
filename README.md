## DoyaChat: 相手の表情がわかるチャットアプリ

[TechCrunch Tokyo 2013のハッカソン](http://jp.techcrunch.com/events/techcrunch-tokyo-2013/hackathon/)で開発，AppSocially賞をいただきました. 
画像認識APIによって，テキスト入力中の相手の表情が伝わるチャットアプリです．

- GCMを利用した1:1でのチャット
- フロントカメラの画像を定期的にサーバに送信 (端末によってはシャッター音が鳴ってしまうようです)
- サーバからのpushに応じてアバターの表情変化

という機能を実装しました．

やっつけコードですが，せっかくなので公開しちゃいます．

#### チームメンバ
Kazuki, Takuya, Yuto, Takumi, Tatsuo, (Yusuke)

---

#### どうでも良い補足

動作のためには別途公開されるかもしれないサーバ側コードの他に，
[Google Cloud Console](https://cloud.google.com/console#/project)からアプリを登録し，取得したアプリキーを

> id=(GCMのアプリキー)

という形式で _assets/gcm.properties_ ファイルに書いて置く必要があります．
