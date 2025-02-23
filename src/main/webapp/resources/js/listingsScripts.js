function exportToExcel() {
    let table = document.querySelector("[id$='CPList']");
    if (!table) {
        console.error("Table not found!");
        return;
    }

    let rows = table.querySelectorAll("tbody tr");
    let data = [];

    let headerRows = table.querySelectorAll("thead tr");

    if (!headerRows || headerRows.length < 3) {
        return;
    }

    let firstDataRow = [];
    headerRows[1].querySelectorAll("th").forEach(th => {
        let headerText = th.innerText.trim();
        if (headerText) {
            firstDataRow.push(headerText);
        }
    });

    let secondDataRow = new Array(5).fill(""); // Shift by 5 empty cells
    headerRows[2].querySelectorAll("th").forEach(th => {
        let headerText = th.innerText.trim();
        if (headerText) {
            secondDataRow.push(headerText);
        }
    });

    data.push(firstDataRow);
    data.push(secondDataRow);

    rows.forEach(row => {
        let rowData = [];
        row.querySelectorAll("td").forEach(td => {
            rowData.push(td.innerText.trim());
        });

        // Only push rows that contain actual data
        if (rowData.some(cell => cell !== "")) {
            data.push(rowData);
        }
    });

    let ws = XLSX.utils.aoa_to_sheet(data);

    Object.keys(ws).forEach(cell => {
        if (!cell.startsWith("!")) {
            ws[cell].z = '@';
        }
    });

    let colWidths = data[0].map((col, i) => ({
        wch: Math.max(...data.map(row => row[i]?.length || 10)) + 2
    }));

    ws['!cols'] = colWidths;

    let wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "DataTable");

    XLSX.writeFile(wb, "datatable_export.xlsx");
}
