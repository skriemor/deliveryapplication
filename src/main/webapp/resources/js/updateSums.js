function updateColumnSums() {
    try {
        let table = document.querySelector("[id$='CPList']");
        if (!table) {
            console.error("Table #CPList not found!");
            return;
        }

        document.querySelectorAll("[id$='CPList'] tbody tr").forEach(function (row, rowIndex) {
            let cells = row.querySelectorAll("td");

            if (cells.length < 12) {
                console.warn(`Skipping row ${rowIndex} because it has ${cells.length} columns`);
                return;
            }

            let sum = 0;

            for (var i = 6; i < 12; i++) {
                if (cells[i]) {
                    let cellValue = parseFloat(cells[i].textContent.trim().replace(/\s/g, '').replace(',', '.')) || 0;
                    sum += cellValue;
                } else {
                    console.warn(`Column ${i} not found in row ${rowIndex}`);
                }
            }

            let totalCell = row.querySelector(".sixSum");
            if (totalCell) {
                totalCell.innerText = Math.round(sum);
            }
        });

    } catch (error) {
        console.error("Error in updateColumnSums:", error);
    }
}

document.addEventListener("DOMContentLoaded", function () {
    updateColumnSums();
});
