package me.centralworks.modules.punishments.models.supliers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.modules.punishments.models.Punishment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class PlaceHolder {

    private List<String> list;
    private Punishment punishment;

    public PlaceHolder(List<String> list, Punishment punishment) {
        this.list = list;
        this.punishment = punishment;
    }

    public List<String> applyPlaceHolders() {
        return list.stream().map(s -> s
                .replace("&", "§")
                .replace("{finishAt}", punishment.getData().isPermanent() ? "§cPermanente." : new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(punishment.getData().getFinishDate())
                        .replace("-", " às "))
                .replace("{author}", punishment.getData().getPunisher())
                .replace("{id}", punishment.getId().toString())
                .replace("{evidence}", punishment.getData().getEvidencesFinally())
                .replace("{nickname}", punishment.getSecondaryIdentifier())
                .replace("{startedAt}", new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(punishment.getData().getStartDate())
                        .replace("-", " às "))
                .replace("{reason}", punishment.getData().getReason().getReason()))
                .collect(Collectors.toList());
    }
}
