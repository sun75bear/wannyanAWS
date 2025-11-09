// 定数：動作範囲のサイズ
const BOUNDARY_WIDTH = 640;
const BOUNDARY_HEIGHT = 480;
const PET_SIZE = 100; // CSSで設定した.petのwidth/height

// 定数：ペットが完全に収まる最大座標
const MAX_LEFT = BOUNDARY_WIDTH - PET_SIZE; // 640 - 100 = 540
const MAX_TOP = BOUNDARY_HEIGHT - PET_SIZE;  // 480 - 100 = 380


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
        if (dogCount > catCount) {
            document.body.className = "dog-bg"; // 犬が多い場合
            console.log(`犬が ${dogCount} 匹で優勢です。背景を犬用 (dog-bg) に設定しました。`);
            // ★追加デバッグ: 実際に設定されたクラスを確認
            console.log("bodyに設定されたクラス名 (直後):", document.body.className);
        } else if (catCount > dogCount) {
            document.body.className = "cat-bg"; // 猫が多い場合
            console.log(`猫が ${catCount} 匹で優勢です。背景を猫用 (cat-bg) に設定しました。`);
            // ★追加デバッグ: 実際に設定されたクラスを確認
            console.log("bodyに設定されたクラス名 (直後):", document.body.className);
        } else {
            document.body.className = "neutral-bg"; // 同数の場合 (任意)
            console.log("犬と猫が同数です。背景をデフォルト/中立 (neutral-bg) に設定しました。");
            // ★追加デバッグ: 実際に設定されたクラスを確認
            console.log("bodyに設定されたクラス名 (直後):", document.body.className);
        }
        // ===============================================

// 🐾 変更点 1: 配置先を #movement-boundary に変更 🐾
        const area = document.getElementById("movement-boundary");
        if (!area) {
            throw new Error("#movement-boundary 要素が見つかりません。HTMLを確認してください。");
        }
        
        pets.forEach(pet => {
            const img = document.createElement("img");
            
            img.src = pet["dogcat"] === "犬" ? "img/sdrun_inframe/inusd01.png" : "img/sdrun_inframe/nekosd01.png";
            img.className = "pet";
            img.title = `${pet["name"]}: ${pet["text"]}`;
            area.appendChild(img); // #movement-boundary に追加

            // 🐾 変更点 2: 移動範囲を固定値 (540, 380) に変更 🐾
            function setRandomPosition() {
                // MAX_LEFT (540) と MAX_TOP (380) を利用し、画像が境界内に収まるようにする
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