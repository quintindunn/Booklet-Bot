package com.t1ps;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Scanner;  // Import the Scanner class
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        // Bot settings:
        String game;
        String base_name;
        if (true){
            System.out.print("Bot Name: ");
            base_name = scanner.nextLine();
            System.out.print("Game URL: ");
            game = scanner.nextLine();
        } else {
            base_name = "bot";
            game = "https://www.blooket.com/play?id=442806";
        }

        Random r = new Random();
        int low = 1;
        int high = 1000;
        int result = r.nextInt(high-low) + low;
        String nickname = base_name + result;

        // Initialize Driver
        System.setProperty("webdriver.chrome.driver","C:\\Users\\T1ps\\CustomCMD\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        // Initialize Map
        Map<String, String> map = new HashMap<>();

        // Get booklet page
        driver.get(game);

        // Submit nickname
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebDriverWait wait1 = new WebDriverWait(driver, 0);
        WebElement nick_input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/form/div[2]/input")));
        driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/form/div[2]/input"));
        nick_input.sendKeys(nickname);
        nick_input.sendKeys(Keys.ENTER);
        System.out.println("Nickname Submitted: " + nickname);

        // Accept
        try {
            wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("")));
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[3]/form/div[2]/div/div")).click();
        } catch (Exception ignored) {}




        // Answer Loop:
        boolean skipped = false;
        while (true) {
            if (Objects.equals(driver.getCurrentUrl(), "https://dashboard.blooket.com/play/racing/final")){
                driver.close();
                break;
            }
            if (!skipped){
                try{
                    driver.findElement(By.xpath("/html/body/div/div/div/div[3]/form/div[2]/div/div")).click();
                    skipped = true;
                } catch (Exception ignored){}
            }

            try {

            // Handle Answering:

            // Wait for question:
                WebElement question = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div[1]/div/div")));
                String question_text = question.getText();
                List<WebElement> answers = driver.findElements(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div/div[2]/div[*]"));
                String answer_text = null;
                boolean skip = false;
                while (true) {

                    try {

                        if (Objects.equals(driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div[1]")).getText(), "Power-Up")) {
                            skip = true;

                            break;
                        }
                    }
                    catch (Exception ignored) {}
                    try {
                        for (WebElement answer : answers) {
                            // If answer is known:
                            answer_text = answer.getText();

                            if (map.containsKey(question_text)) {
                                if (Objects.equals("Correct Answer: " + answer_text, map.get(question_text))) {
                                    answer.click();
                                    break;

                                }
                            } else {

                                answer.click();
                                break;
                            }


                        }
                        break;
                    } catch (Exception ignored){}
                }
                if (!(skip)) {
                    boolean correct;
                    if (Objects.equals(driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div/div[1]")).getAttribute("innerHTML"), "CORRECT")) {
                        map.put(question_text, "Correct Answer: " + answer_text);
                        correct = true;
                    } else {
                        String correct_answer = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div/div[3]")).getAttribute("innerHTML");
                        map.put(question_text, correct_answer);
                        correct = false;

                    }
                    if (correct) {
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div/div[3]"))).click();
                    } else {
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div/div[4]"))).click();
                    }

                }
                handle_power_up(driver);
            }
            catch (Exception TimeoutException) {
                handle_power_up(driver);
            }


        }






    }

    private static void handle_power_up(WebDriver driver) {
        try {
            if (Objects.equals(driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div[1]")).getText(), "Power-Up")) {
                driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div[3]")).click();
            }
        } catch (Exception ignored) {}
        try {
            if (Objects.equals(driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div[1]")).getText(), "Choose 1 Player")) {
                driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[2]/div/div[2]/div")).click();
            }
        } catch (Exception ignored) {}
    }

}