function Count-Substrings {

    param (
        $substrings
    )

    $TagSubstrings = @("FriskTags.PATIENCE", "FriskTags.BRAVERY", "FriskTags.KINDNESS", "FriskTags.INTEGRITY", "FriskTags.JUSTICE", "FriskTags.PERSEVERANCE", "FriskTags.YOU", "CardTags.STARTER_STRIKE", "CardTags.STARTER_DEFEND")

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

    # Process each file
    foreach ($file in $files) {

        # Read file content as a single string
        if ($($file.Name).Substring(0, 3).equals("OLD")){
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
            if ($TagCount -gt 0){
                foreach ($substring in $substrings) {
                    $count = ([regex]::Matches($content, [regex]::Escape($substring))).Count
                    $totalCounts[$substring] += $count
                }
                break
            }
        }
    }

    # Output results
    Write-Host "`n"
    foreach ($substring in $substrings) {
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
Count-Substrings -substrings $CardSubstrings

$CostSubstrings = @("0 //The card's base cost", "1 //The card's base cost", "2 //The card's base cost", "3 //The card's base cost", "4 //The card's base cost", "-1 //The card's base cost", "-2 //The card's base cost")
Write-Host "===Card Costs==="
Count-Substrings -substrings $CostSubstrings

Read-Host "Press Enter to continue..."
