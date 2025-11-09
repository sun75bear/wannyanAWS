// 今後、もしPET_SIZEを大きくしたい場合はここで変更（例: 100 -> 200）
const PET_SIZE = 75; // キャラクターサイズ（CSSと合わせるか、JSでスタイル設定する。現在は100px指定）
// // 定数：動作範囲のサイズをウィンドウの現在値で定義
// 単体表示モードでは、ウィンドウ全体の８割が動作範囲になります。
const BOUNDARY_WIDTH = window.innerWidth * 0.5;
const BOUNDARY_HEIGHT = window.innerHeight * 0.6;

// 定数：ペットが完全に収まる最大座標を再計算
// 画面幅/高さからペットサイズを引く
const MAX_LEFT = BOUNDARY_WIDTH - PET_SIZE; 
const MAX_TOP = BOUNDARY_HEIGHT - PET_SIZE;


async function loadPets() {
    try {
        // 1. データ取得
        const response = await fetch('/PetDataServlet');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const pets = await response.json();
        
        // ★★★ デバッグ用追加コード ★★★
        console.log("取得したペットデータ:", pets);
        console.log("データ件数:", pets.length);
        // ★★★ ここまで ★★★
        // ===============================================
        // 【★ 追加: 犬と猫の数をカウントし、背景を設定するロジック ★】
        // ===============================================
        let dogCount = 0;
        let catCount = 0;
        
        pets.forEach(pet => {
            if (pet["dogcat"] === "犬") {
                dogCount++;
            } else if (pet["dogcat"] === "猫") {
                catCount++;
            }
        });
        
        // 2. 多い方の背景を設定
        const backgroundElement = document.getElementById("character-area");
        // if (backgroundElement) { } のチェックを入れると安全ですが、ここでは省略

        if (dogCount > catCount) {
            // 変更: document.body.className を backgroundElement.className に
            backgroundElement.className = "dog-bg";
            document.body.className = "dog-bg-body"; // bodyのスクロール対策として残しても良い

        } else if (catCount > dogCount) {
            // 変更: document.body.className を backgroundElement.className に
            backgroundElement.className = "cat-bg";
            document.body.className = "cat-bg-body";

        } else {
            // 変更: document.body.className を backgroundElement.className に
            backgroundElement.className = "neutral-bg";
            document.body.className = "neutral-bg-body";
        }
        // ===============================================

// 🐾 変更点 1: 配置先を #movement-boundary に変更 🐾
        const area = document.getElementById("movement-boundary");
        if (!area) {
            throw new Error("#movement-boundary 要素が見つかりません。HTMLを確認してください。");
        }
        
        pets.forEach(pet => {
            const img = document.createElement("img");
            
            img.src = pet["dogcat"] === "犬" ? "img/sdrun_all/inusd01.png" : "img/sdrun_all/nekosd01.png";
            img.className = "pet";
            img.title = `${pet["name"]}: ${pet["text"]}`;
            area.appendChild(img); // #movement-boundary に追加

            function setRandomPosition() {
                const newLeft = Math.random() * MAX_LEFT;
                const newTop = Math.random() * MAX_TOP;
                
                img.style.left = newLeft + "px";
                img.style.top = newTop + "px";
            }
            
            // 初回ロード時にランダムな位置に配置する
            setRandomPosition(); 

            // 3秒ごとにランダム移動を繰り返す
            setInterval(setRandomPosition, 3000);
        });
    } catch (err) {
        // エラー発生時はコンソールに出力
        console.error("データ取得に失敗:", err);
    }
}

document.addEventListener("DOMContentLoaded", loadPets);