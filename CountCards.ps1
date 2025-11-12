function Count-Substrings {

    param (
        $substrings,
        [bool]$display = $false
    )

    $TagSubstrings = @(
        "FriskTags.PATIENCE", "FriskTags.BRAVERY", "FriskTags.KINDNESS",
        "FriskTags.INTEGRITY", "FriskTags.JUSTICE", "FriskTags.PERSEVERANCE",
        "FriskTags.YOU", "CardTags.STARTER_STRIKE", "CardTags.STARTER_DEFEND"
    )

    $folderPath = "C:\Users\James\IdeaProjects\BasicMod\src\main\java\friskmod\cards"

    # Whether to include subfolders (set to $false if not)
    $includeSubfolders = $true

    # Get all files from the folder
    $files = Get-ChildItem -Path $folderPath -Recurse:$includeSubfolders -File

    # Initialise a hashtable to store total counts
    $totalCounts = @{}
    foreach ($substring in $substrings) {
        $totalCounts[$substring] = 0
    }

    # Hashtable to track which files contained each substring
    $foundInFiles = @{}
    foreach ($substring in $substrings) {
        $foundInFiles[$substring] = @()
    }

    # Process each file
    foreach ($file in $files) {

        # Skip files starting with "OLD"
        if ($($file.Name).Substring(0, 3).Equals("OLD")) {
            Continue
        }

        try {
            $content = Get-Content -Path $file.FullName -Raw -ErrorAction Stop
        } catch {
            Write-Warning "Could not read file: $($file.FullName)"
            continue
        }

        # Count occurrences for each substring
        foreach ($TagSubstring in $TagSubstrings) {
            $TagCount = ([regex]::Matches($content, [regex]::Escape($TagSubstring))).Count
            if ($TagCount -gt 0) {
                foreach ($substring in $substrings) {
                    $count = ([regex]::Matches($content, [regex]::Escape($substring))).Count
                    if ($count -gt 0) {
                        $totalCounts[$substring] += $count
                        # Track which files contained this substring
                        $foundInFiles[$substring] += $file.Name
                    }
                }
                break
            }
        }
    }

    # Output results
    Write-Host "`n"
    foreach ($substring in $substrings) {

        if ($display -and $foundInFiles[$substring].Count -gt 0) {
            Write-Host "Files containing '$substring':"
            foreach ($f in $foundInFiles[$substring] | Sort-Object -Unique) {
                Write-Host "  $f"
            }
            Write-Host "============================"
        }
        Write-Host ("'{0}' occurred {1} times" -f $substring, $totalCounts[$substring])
    }

    $totalSum = ($totalCounts.Values | Measure-Object -Sum).Sum
    Write-Host "`nTotal occurrences across all substrings: $totalSum`n"
}


$TagSubstrings = @("FriskTags.PATIENCE", "FriskTags.BRAVERY", "FriskTags.KINDNESS", "FriskTags.INTEGRITY", "FriskTags.JUSTICE", "FriskTags.PERSEVERANCE", "FriskTags.YOU", "CardTags.STARTER_STRIKE", "CardTags.STARTER_DEFEND")
Write-Host "===Total cards==="
Count-Substrings -substrings $TagSubstrings

$CardSubstrings = @("CardType.ATTACK", "CardType.POWER", "CardType.SKILL")
Write-Host "===Card types==="
Count-Substrings -substrings $CardSubstrings

$CardSubstrings = @("CardRarity.BASIC", "CardRarity.COMMON", "CardRarity.UNCOMMON", "CardRarity.RARE")
Write-Host "===Card rarities==="
Count-Substrings -substrings $CardSubstrings -display 1

$CostSubstrings = @("0 //The card's base cost", "1 //The card's base cost", "2 //The card's base cost", "3 //The card's base cost", "4 //The card's base cost", "-1 //The card's base cost", "-2 //The card's base cost")
Write-Host "===Card Costs==="
Count-Substrings -substrings $CostSubstrings

Read-Host "Press Enter to continue..."
