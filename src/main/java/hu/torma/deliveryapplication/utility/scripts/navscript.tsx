import * as puppeteer from 'puppeteer';
import * as cheerio from 'cheerio';

(async () => {
    const browser = await puppeteer.launch();
    const page = await browser.newPage();
    await page.goto('https://portal.nebih.gov.hu/felir-kereso');

    await page.type('#searchNaturalPersonalNameValue', 'Dudics János');
    await page.type('#searchNaturalPersonalBirthDateValue', '1955-07-24');


    await page.waitForNavigation();

    const html = await page.content();
    const $ = cheerio.load(html);

    const result = $('div.result').text();
    console.log(result);

    await browser.close();
})();